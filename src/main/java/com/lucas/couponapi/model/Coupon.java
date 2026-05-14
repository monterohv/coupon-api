package com.lucas.couponapi.model;

import com.lucas.couponapi.exception.BusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Coupon", description = "Representação de um cupom no sistema")
public class Coupon {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    @Schema(description = "Identificador único (UUID)", example = "cef9d1e3-aae5-4ab6-a297-358c6032b1e7")
    private UUID id;

    @Schema(description = "Código sanitizado do cupom (sempre 6 caracteres)", example = "ABC123")
    @Column(nullable = false)
    private String code;

    @Schema(description = "Descrição do cupom", example = "Desconto de Natal")
    @Column(length = 1000, nullable = false)
    private String description;

    @Schema(description = "Valor do desconto", example = "25.00")
    @Column(nullable = false)
    private BigDecimal discountValue;

    @Schema(description = "Data em que o cupom perde a validade", example = "2026-12-31T23:59:59")
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Schema(description = "Indica se o cupom está publicado para uso", example = "true")
    private boolean published;

    @Schema(description = "Indica se o cupom já foi utilizado/resgatado", example = "false")
    private boolean redeemed = false;

    @Schema(description = "Indica se o cupom já foi deletado", example = "false")
    private boolean deleted = false;

    public Coupon(String code, String description, BigDecimal discountValue, LocalDateTime expirationDate, boolean published) {
        this.code = sanitizeCode(code);
        validateDiscount(discountValue);
        validateExpiration(expirationDate);
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
    }

    private String sanitizeCode(String code) {
        if (code == null) throw new BusinessException("O código é obrigatório.");
        String sanitized = code.replaceAll("[^a-zA-Z0-9]", "");
        if (sanitized.length() != 6) {
            throw new BusinessException("O código deve ter exatamente 6 caracteres alfanuméricos.");
        }
        return sanitized;
    }

    private void validateDiscount(BigDecimal value) {
        if (value == null || value.compareTo(new BigDecimal("0.5")) < 0) {
            throw new BusinessException("O valor de desconto mínimo é 0.5");
        }
    }

    private void validateExpiration(LocalDateTime date) {
        if (date == null || date.isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data de expiração não pode ser no passado.");
        }
    }
}