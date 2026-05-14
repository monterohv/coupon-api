package com.lucas.couponapi.controller;

import com.lucas.couponapi.dto.CouponDTO;
import com.lucas.couponapi.model.Coupon;
import com.lucas.couponapi.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "Recursos para gerenciamento de cupons de desconto")
public class CouponController {

    private final CouponService service;

    @Operation(
            summary = "Cria um novo cupom",
            description = "Cadastra um cupom no sistema. O código será sanitizado (removendo caracteres especiais) " +
                    "e validado para ter exatamente 6 caracteres. O desconto mínimo é 0.5."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso",
                    content = @Content(schema = @Schema(implementation = Coupon.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou violação de regra de negócio",
                    content = @Content(schema = @Schema(example = "{\"message\": \"O desconto mínimo é 0,5.\"}")))
    })
    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody @Valid CouponDTO dto) {
        Coupon newCoupon = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCoupon);
    }

    @Operation(
            summary = "Busca um cupom por ID",
            description = "Retorna os detalhes de um cupom específico. Se o cupom tiver sofrido exclusão lógica, ele não será encontrado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupom encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Coupon.class))),
            @ApiResponse(responseCode = "400", description = "Cupom não encontrado ou já removido",
                    content = @Content(schema = @Schema(example = "{\"message\": \"Cupom não encontrado.\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Coupon> findById(
            @Parameter(description = "ID numérico do cupom", example = "1")
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Remove um cupom",
            description = "Realiza a exclusão lógica (soft delete) de um cupom através do seu ID. " +
                    "Caso o cupom já tenha sido deletado ou não exista, retornará um erro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cupom removido com sucesso (Soft Delete)"),
            @ApiResponse(responseCode = "400", description = "Cupom não encontrado ou já deletado",
                    content = @Content(schema = @Schema(example = "{\"message\": \"Cupom não encontrado ou já removido.\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID numérico do cupom a ser removido", example = "1")
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}