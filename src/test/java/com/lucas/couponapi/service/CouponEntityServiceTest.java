package com.lucas.couponapi.service;

import com.lucas.couponapi.dto.CouponDTO;
import com.lucas.couponapi.exception.BusinessException;
import com.lucas.couponapi.mapper.CouponMapper;
import com.lucas.couponapi.model.CouponEntity;
import com.lucas.couponapi.model.domain.Coupon;
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
class CouponEntityServiceTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private CouponService service;

    @Mock
    private CouponMapper mapper;

    UUID id = UUID.randomUUID();

    @Test
    @DisplayName("Criação válida: Deve salvar cupom quando DTO for íntegro")
    void deveCriarCupomValidoESalvarNoBanco() {

        CouponDTO dto = new CouponDTO(
                "DESC10",
                "10% OFF",
                BigDecimal.valueOf(10.0),
                LocalDateTime.now().plusDays(10),
                true
        );

        CouponEntity entity = new CouponEntity();

        when(mapper.toEntity(any(Coupon.class)))
                .thenReturn(entity);

        when(repository.save(any(CouponEntity.class)))
                .thenReturn(entity);

        CouponEntity result = service.create(dto);

        assertNotNull(result);

        verify(mapper, times(1))
                .toEntity(any(Coupon.class));

        verify(repository, times(1))
                .save(any(CouponEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover cupom inexistente")
    void deveLancarExcecaoAoRemoverCupomInexistente() {
        when(repository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

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
        when(repository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> service.delete(id));
        assertEquals("Cupom não encontrado ou já removido.", ex.getMessage());
    }

    @Test
    @DisplayName("Soft Delete: Deve marcar o cupom como deletado sem removê-lo do banco")
    void shouldSoftDeleteCoupon() {

        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(id);
        couponEntity.setDeleted(false);

        when(repository.findByIdAndDeletedFalse(id))
                .thenReturn(Optional.of(couponEntity));

        service.delete(id);

        assertTrue(couponEntity.isDeleted());

        verify(repository).save(couponEntity);
    }

    @Test
    @DisplayName("Deve retornar um cupom quando o ID existir no banco de dados")
    void deveRetornarCupomQuandoIdExistir() {
        CouponEntity mockCouponEntity = new CouponEntity(
                null,
                "ABC123",
                "Cupom de Natal",
                new BigDecimal("25.00"),
                LocalDateTime.now().plusDays(1),
                true,
                false,
                false
        );

        when(repository.findById(id)).thenReturn(Optional.of(mockCouponEntity));

        CouponEntity result = service.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("ABC123");
        assertThat(result.getDiscountValue()).isEqualByComparingTo("25.00");

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