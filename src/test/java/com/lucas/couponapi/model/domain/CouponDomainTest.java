package com.lucas.couponapi.model.domain;

import com.lucas.couponapi.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponDomainTest {

    private static final String VALID_CODE = "ABC123";

    @Test
    @DisplayName("Deve sanitizar o código removendo caracteres especiais e mantendo 6 caracteres")
    void deveSanitizarCodigoComSucesso() {

        Coupon couponDomain = new Coupon(
                "ABC-123!",
                "Promo",
                new BigDecimal("10.0"),
                LocalDateTime.now().plusDays(1),
                true
        );

        assertThat(couponDomain.getCode()).isEqualTo(VALID_CODE);
    }

    @Test
    @DisplayName("Deve lançar exceção se o código sanitizado não tiver 6 caracteres")
    void deveLancarExcecaoQuandoCodigoForInvalido() {

        BigDecimal discount = new BigDecimal("10.0");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

        assertThatThrownBy(() ->
                new Coupon(
                        "A1B2",
                        "Desc",
                        discount,
                        expirationDate,
                        true
                )
        )
                .isInstanceOf(BusinessException.class)
                .hasMessage("O código deve possuir exatamente 6 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção para desconto menor que 0.5")
    void deveLancarExcecaoParaDescontoBaixo() {

        BigDecimal discount = new BigDecimal("0.49");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

        assertThatThrownBy(() ->
                new Coupon(
                        VALID_CODE,
                        "Desc",
                        discount,
                        expirationDate,
                        true
                )
        )
                .isInstanceOf(BusinessException.class)
                .hasMessage("O desconto mínimo deve ser 0.5");
    }

    @Test
    @DisplayName("Deve lançar exceção para data de expiração no passado")
    void deveLancarExcecaoParaDataDeExpiracaoNoPassado() {

        BigDecimal discount = new BigDecimal("10.0");
        LocalDateTime pastDate = LocalDateTime.now().minusMinutes(1);

        assertThatThrownBy(() ->
                new Coupon(
                        VALID_CODE,
                        "Desc",
                        discount,
                        pastDate,
                        true
                )
        )
                .isInstanceOf(BusinessException.class)
                .hasMessage("A data deve ser no futuro");
    }

    @Test
    @DisplayName("Deve publicar cupom com sucesso")
    void devePublicarCupomComSucesso() {

        Coupon couponDomain = new Coupon(
                VALID_CODE,
                "Promoção",
                new BigDecimal("10.0"),
                LocalDateTime.now().plusDays(5),
                false
        );

        couponDomain.publish();

        assertThat(couponDomain.isPublished()).isTrue();
    }

    @Test
    @DisplayName("Deve despublicar cupom com sucesso")
    void deveDespublicarCupomComSucesso() {

        Coupon couponDomain = new Coupon(
                VALID_CODE,
                "Promoção",
                new BigDecimal("10.0"),
                LocalDateTime.now().plusDays(5),
                true
        );

        couponDomain.unpublish();

        assertThat(couponDomain.isPublished()).isFalse();
    }

}