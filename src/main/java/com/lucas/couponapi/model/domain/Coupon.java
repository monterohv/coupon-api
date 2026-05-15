package com.lucas.couponapi.model.domain;


import com.lucas.couponapi.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Coupon {
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private boolean published;

    public Coupon(String code, String description, BigDecimal discountValue, LocalDateTime expirationDate, boolean published) {
        this.code = validateAndCleanCode(code);
        this.description = validateDescription(description);
        this.discountValue = validateDiscount(discountValue);
        this.expirationDate = validateExpiration(expirationDate);
        this.published = published;
    }

    private String validateAndCleanCode(String code) {

        if (code == null || code.isBlank()) {
            throw new BusinessException("O código é obrigatório");
        }

        String cleaned = code
                .replaceAll("[^a-zA-Z0-9]", "")
                .toUpperCase();

        if (cleaned.isBlank()) {
            throw new BusinessException("Código inválido após limpeza");
        }

        if (cleaned.length() != 6) {
            throw new BusinessException("O código deve possuir exatamente 6 caracteres");
        }

        return cleaned;
    }

    private BigDecimal validateDiscount(BigDecimal value) {
        if (value == null) throw new BusinessException("O valor de desconto é obrigatório");
        if (value.compareTo(new BigDecimal("0.5")) < 0) {
            throw new BusinessException("O desconto mínimo deve ser 0.5");
        }
        return value;
    }

    private LocalDateTime validateExpiration(LocalDateTime date) {
        if (date == null) throw new BusinessException("A data de expiração é obrigatória");
        if (date.isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data deve ser no futuro");
        }
        return date;
    }

    private String validateDescription(String desc) {
        if (desc == null || desc.isBlank()) throw new BusinessException("A descrição é obrigatória");
        return desc;
    }

    public void publish() {
        this.published = true;
    }

    public void unpublish() {
        this.published = false;
    }
}