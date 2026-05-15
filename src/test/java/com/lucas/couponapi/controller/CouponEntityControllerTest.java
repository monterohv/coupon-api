package com.lucas.couponapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lucas.couponapi.dto.CouponDTO;
import com.lucas.couponapi.model.CouponEntity;
import com.lucas.couponapi.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CouponService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    CouponEntity couponEntity = new CouponEntity(
            null,
            "ABC123",
            "Cupom de Natal",
            new BigDecimal("25.00"),
            LocalDateTime.now().plusDays(1),
            true,
            false,
            false
    );


    @Test
    @DisplayName("POST /coupon - Deve retornar 201 Created ao criar um cupom válido")
    void deveCriarCupomERetornar201() throws Exception {
        CouponDTO dto = new CouponDTO("ABC-123", "Cupom de Natal", new BigDecimal("25.00"), LocalDateTime.now().plusDays(1), true);

        when(service.create(any(CouponDTO.class))).thenReturn(couponEntity);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.discountValue").value(25.00));
    }

    @Test
    @DisplayName("GET /coupon/{id} - Deve retornar 200 OK quando o cupom existir")
    void deveRetornarCupomQuandoExistir() throws Exception {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        when(service.findById(id)).thenReturn(couponEntity);

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ABC123"));

        verify(service, times(1)).findById(id);
    }

    @Test
    @DisplayName("DELETE /coupon/{id} - Deve retornar 204 No Content ao deletar com sucesso")
    void deveRetornar204QuandoDeletado() throws Exception {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(id);
    }
}