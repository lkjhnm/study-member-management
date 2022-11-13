package com.grasstudy.user.service;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.repository.UserRepository;
import com.grasstudy.user.support.MockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;

	@Test
	void signup() {
		User mockUser = MockBuilder.getMockUser("mock-id");
		Mockito.when(userRepository.save(any())).thenReturn(Mono.just(mockUser));

		StepVerifier.create(userService.signup(mockUser))
				.expectNext(ResponseEntity.ok().<Void>build())
				.verifyComplete();
	}

	@Test
	void signup_fail_test() {
		Mockito.when(userRepository.save(any())).thenReturn(Mono.error(new RuntimeException("mock error")));

		User mockUser = MockBuilder.getMockUser("mock-id");
		StepVerifier.create(userService.signup(mockUser))
		            .expectNext(ResponseEntity.internalServerError().build())
		            .verifyComplete();
	}
}