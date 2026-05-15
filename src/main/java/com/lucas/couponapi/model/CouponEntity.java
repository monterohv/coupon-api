package com.lucas.couponapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representação do cupom na base de dados")
public class CouponEntity {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    @Schema(description = "Identificador único (UUID)", example = "cef9d1e3-aae5-4ab6-a297-358c6032b1e7")
    private UUID id;

    @Column(nullable = false, length = 6)
    @Schema(description = "Código alfanumérico único de 6 caracteres", example = "SUMMER26")
    private String code;

    @Schema(description = "Descrição do cupom", example = "Desconto de Natal")
    @Column(length = 1000, nullable = false)
    private String description;

    @Schema(description = "Valor do desconto", example = "0.8")
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
}