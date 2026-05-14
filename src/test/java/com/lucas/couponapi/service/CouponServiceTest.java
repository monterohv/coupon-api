package com.lucas.couponapi.service;

import com.lucas.couponapi.dto.CouponDTO;
import com.lucas.couponapi.exception.BusinessException;
import com.lucas.couponapi.model.Coupon;
import com.lucas.couponapi.repositoy.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private CouponService service;

    UUID id = UUID.randomUUID();

    @Test
    @DisplayName("Criação válida: Deve salvar cupom quando DTO for íntegro")
    void deveCriarCupomValidoESalvarNoBanco() {
        CouponDTO dto = new CouponDTO("DESC10", "10% OFF", BigDecimal.valueOf(10.0), LocalDateTime.now().plusDays(10), true);
        when(repository.save(any(Coupon.class))).thenReturn(new Coupon());

        Coupon result = service.create(dto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Coupon.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover cupom inexistente")
    void deveLancarExcecaoAoRemoverCupomInexistente() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.delete(id)
        );

        assertEquals(
                "Cupom não encontrado ou já removido.",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Delete Duplicado: Deve lançar erro se o cupom já não existir")
    void deveLancarErroAoTentarDeletarCupomDuplicado() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> service.delete(id));
        assertEquals("Cupom não encontrado ou já removido.", ex.getMessage());
    }

    @Test
    @DisplayName("Soft Delete: Deve marcar o cupom como deletado sem removê-lo do banco")
    void shouldSoftDeleteCoupon() {

        Coupon coupon = new Coupon();
        coupon.setId(id);
        coupon.setDeleted(false);

        when(repository.findById(id))
                .thenReturn(Optional.of(coupon));

        service.delete(id);

        assertTrue(coupon.isDeleted());

        verify(repository).save(coupon);
    }

    @Test
    @DisplayName("Deve retornar um cupom quando o ID existir no banco de dados")
    void deveRetornarCupomQuandoIdExistir() {
        Coupon mockCoupon = new Coupon(
                "OFF100",
                "Desconto de Teste",
                new BigDecimal("100.00"),
                LocalDateTime.now().plusDays(5),
                true
        );

        when(repository.findById(id)).thenReturn(Optional.of(mockCoupon));

        Coupon result = service.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("OFF100");
        assertThat(result.getDiscountValue()).isEqualByComparingTo("100.00");

        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando o cupom não for encontrado")
    void deveLancarExcecaoQuandoCupomNaoForEncontrado() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cupom não encontrado.");

        verify(repository, times(1)).findById(id);
    }

}