package com.lucas.couponapi.service;

import com.lucas.couponapi.dto.CouponDTO;
import com.lucas.couponapi.exception.BusinessException;
import com.lucas.couponapi.model.CouponEntity;
import com.lucas.couponapi.model.domain.Coupon;
import com.lucas.couponapi.repositoy.CouponRepository;
import lombok.RequiredArgsConstructor;
import com.lucas.couponapi.mapper.CouponMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository repository;
    private final CouponMapper mapper;

    @Transactional
    public CouponEntity create(CouponDTO dto) {
        Coupon domain = new Coupon(
                dto.code(),
                dto.description(),
                dto.discountValue(),
                dto.expirationDate(),
                dto.published()
        );

        CouponEntity couponEntity = mapper.toEntity(domain);
        return repository.save(couponEntity);
    }

    @Transactional(readOnly = true)
    public CouponEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cupom não encontrado."));
    }

    @Transactional
    public void delete(UUID id) {
        CouponEntity couponEntity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new BusinessException("Cupom não encontrado ou já removido."));

        couponEntity.setDeleted(true);
        repository.save(couponEntity);
    }
}