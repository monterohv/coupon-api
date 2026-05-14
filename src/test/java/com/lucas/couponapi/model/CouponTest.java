package com.lucas.couponapi.model;

import com.lucas.couponapi.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    @Test
    @DisplayName("Deve sanitizar o código removendo caracteres especiais e mantendo 6 caracteres")
    void deveSanitizarCodigoComSucesso() {
        Coupon coupon = new Coupon("ABC-123!", "Promo", new BigDecimal("10.0"), LocalDateTime.now().plusDays(1), true);
        assertThat(coupon.getCode()).isEqualTo("ABC123");
    }

    @Test
    @DisplayName("Deve lançar exceção se o código sanitizado não tiver 6 caracteres")
    void deveLancarExcecaoQuandoCodigoForInvalido() {

        BigDecimal discount = new BigDecimal("10.0");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

        assertThatThrownBy(() ->
                new Coupon("A1B2", "Desc", discount, expirationDate, true)
        )
                .isInstanceOf(BusinessException.class)
                .hasMessage("O código deve ter exatamente 6 caracteres alfanuméricos.");
    }

    @Test
    @DisplayName("Deve lançar exceção para desconto menor que 0.5")
    void deveLancarExcecaoParaDescontoBaixo() {

        BigDecimal discount = new BigDecimal("0.49");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

        assertThatThrownBy(() ->
                new Coupon("ABC123", "Desc", discount, expirationDate, true)
        )
                .isInstanceOf(BusinessException.class)
                .hasMessage("O valor de desconto mínimo é 0.5");
    }

    @Test
    @DisplayName("Deve lançar exceção para data de expiração no passado")
    void deveLancarExcecaoParaDataDeExpiracaoNoPassado(){

        BigDecimal discount = new BigDecimal("10.0");
        LocalDateTime pastDate = LocalDateTime.now().minusMinutes(1);

        assertThatThrownBy(() ->
                new Coupon("ABC123", "Desc", discount, pastDate, true)
        )
                .isInstanceOf(BusinessException.class)
                .hasMessage("A data de expiração não pode ser no passado.");
    }
}