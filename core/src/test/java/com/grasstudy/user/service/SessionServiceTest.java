package com.grasstudy.user.service;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.support.MockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@Import({SessionService.class, JwtService.class})
class SessionServiceTest {

	@Autowired
	SessionService sessionService;

	@MockBean
	UserService userService;

	@Test
	void signIn() {
		User mockUser = MockBuilder.getMockUser("mock@mock.com");
		Mockito.when(userService.user(any()))
		       .thenReturn(Mono.just(mockUser));

		sessionService.signIn(mockUser.getEmail(), mockUser.getPassword()).log()
		              .as(StepVerifier::create)
		              .expectNextCount(1)
		              .verifyComplete();
	}

	@Test
	void sign_fail() {
		User mockUser = MockBuilder.getMockUser("mock@mock.com");
		Mockito.when(userService.user(any()))
		       .thenReturn(Mono.just(mockUser));

		sessionService.signIn(mockUser.getEmail(), "failure_password").log()
		              .as(StepVerifier::create)
		              .expectError(RuntimeException.class)
		              .verify();
	}
}