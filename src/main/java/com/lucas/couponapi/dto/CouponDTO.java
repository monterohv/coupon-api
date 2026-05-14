package com.lucas.couponapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Objeto de transferência para criação e resposta de cupons")
public record CouponDTO(

        @Schema(
                description = "Código alfanumérico único. Caracteres especiais serão removidos e o tamanho final deve ser de 6 caracteres.",
                example = "SUMMER26",
                minLength = 1
        )
        @NotBlank(message = "O código é obrigatório")
        String code,

        @Schema(
                description = "Descrição detalhada do propósito do cupom.",
                example = "Cupom de 15% para a primeira compra de verão"
        )
        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @Schema(
                description = "Valor nominal do desconto. Deve ser no mínimo 0.5.",
                example = "15.50"
        )
        @NotNull(message = "O valor de desconto é obrigatório")
        BigDecimal discountValue,

        @Schema(
                description = "Data e hora de expiração do cupom. Deve ser obrigatoriamente uma data futura.",
                example = "2026-12-31T23:59:59"
        )
        @NotNull(message = "A data de expiração é obrigatória")
        @Future(message = "A data deve ser no futuro")
        LocalDateTime expirationDate,

        @Schema(
                description = "Indica se o cupom já deve ser criado como publicado e disponível para uso.",
                example = "true"
        )
        boolean published
) {}