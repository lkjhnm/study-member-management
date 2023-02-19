package com.grasstudy.user.repository;

import com.grasstudy.user.R2DBCConfiguration;
import com.grasstudy.user.entity.User;
import com.grasstudy.user.support.MockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

import java.util.Objects;

@DataR2dbcTest
@ContextConfiguration(classes = R2DBCConfiguration.class)
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Test
	void save() {
		userRepository.save(MockData.getMockUser("mock-id"))
		              .map(User::getEmail)
		              .flatMap(userRepository::findByEmail)
		              .as(StepVerifier::create)
		              .expectNextMatches(member -> Objects.nonNull(member.getId()))
		              .expectComplete();
	}

	@Test
	void duplicated_id_failure_test() {
		User user1 = MockData.getMockUser("mock-id");
		User user2 = MockData.getMockUser("mock-id");

		userRepository.save(user1)
		              .as(StepVerifier::create)
		              .expectNextCount(1)
		              .verifyComplete();

		userRepository.save(user2)
		              .as(StepVerifier::create)
		              .expectError()
		              .verify();
	}
}