package com.grasstudy.user.repository;

import com.grasstudy.user.R2DBCConfiguration;
import com.grasstudy.user.entity.Authentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ContextConfiguration(classes = R2DBCConfiguration.class)
class AuthenticationRepositoryTest {

	@Autowired
	AuthenticationRepository authRepo;

	@Test
	public void save() {
		Authentication mockAuth = Authentication.builder()
		                                     .accessToken("test")
		                                     .refreshToken("test")
		                                     .build();
		authRepo.save(mockAuth)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		authRepo.findById(mockAuth.getId())
				.log()
				.as(StepVerifier::create)
				.expectNext(mockAuth)
				.verifyComplete();
	}
}