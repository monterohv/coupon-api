package com.lucas.couponapi.service;

import com.lucas.couponapi.dto.CouponDTO;
import com.lucas.couponapi.exception.BusinessException;
import com.lucas.couponapi.model.Coupon;
import com.lucas.couponapi.repositoy.CouponRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository repository;

    @Transactional
    public Coupon create(CouponDTO dto) {
        Coupon coupon = new Coupon(
                dto.code(),
                dto.description(),
                dto.discountValue(),
                dto.expirationDate(),
                dto.published()
        );

        return repository.save(coupon);
    }

    @Transactional(readOnly = true)
    public Coupon findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cupom não encontrado."));
    }

    @Transactional
    public void delete(UUID id) {
        Coupon coupon = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cupom não encontrado ou já removido."));

        coupon.setDeleted(true);
        repository.save(coupon);
    }
}