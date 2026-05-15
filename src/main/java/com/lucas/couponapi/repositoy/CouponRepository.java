package com.lucas.couponapi.repositoy;

import com.lucas.couponapi.model.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, UUID> {
    Optional<CouponEntity> findByIdAndDeletedFalse(UUID id);
}
