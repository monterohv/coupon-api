package mapper;

import com.lucas.couponapi.mapper.CouponMapper;
import com.lucas.couponapi.model.CouponEntity;
import com.lucas.couponapi.model.domain.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CouponMapperTest {

    private final CouponMapper mapper =
            Mappers.getMapper(CouponMapper.class);

    @Test
    @DisplayName("Deve converter Domain para Entity")
    void deveConverterDomainParaEntity() {

        Coupon domain = new Coupon(
                "ABC123",
                "Cupom de Natal",
                new BigDecimal("25.00"),
                LocalDateTime.now().plusDays(1),
                true
        );

        CouponEntity entity = mapper.toEntity(domain);

        assertThat(entity).isNotNull();

        assertThat(entity.getCode())
                .isEqualTo(domain.getCode());

        assertThat(entity.getDescription())
                .isEqualTo(domain.getDescription());

        assertThat(entity.getDiscountValue())
                .isEqualTo(domain.getDiscountValue());

        assertThat(entity.getExpirationDate())
                .isEqualTo(domain.getExpirationDate());

        assertThat(entity.isPublished())
                .isEqualTo(domain.isPublished());
    }

}