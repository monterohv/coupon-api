package com.lucas.couponapi.mapper;

import com.lucas.couponapi.model.CouponEntity;
import com.lucas.couponapi.model.domain.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    Coupon toDomain(CouponEntity entity);

    @Mapping(target = "id", ignore = true)
    CouponEntity toEntity(Coupon domain);
}