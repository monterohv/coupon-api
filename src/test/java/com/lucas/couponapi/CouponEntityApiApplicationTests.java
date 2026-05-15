package com.lucas.couponapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponEntityApiApplicationTests {

	@Test
	void contextLoads() {
		CouponApiApplication.main(new String[] {"--spring.profiles.active=test"});
	}

}