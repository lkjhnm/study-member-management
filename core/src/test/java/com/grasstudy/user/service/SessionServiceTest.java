package com.grasstudy.user.service;

import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.entity.User;
import com.grasstudy.user.event.AuthEventPublisher;
import com.grasstudy.user.event.scheme.AuthCreateEvent;
import com.grasstudy.user.event.scheme.AuthExpireEvent;
import com.grasstudy.user.repository.AuthenticationRepository;
import com.grasstudy.user.support.MockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@Import({SessionService.class, JwtService.class, AuthEventPublisher.class, AuthEventService.class})
class SessionServiceTest {

	@Autowired
	SessionService sessionService;

	@Autowired
	JwtService jwtService;

	@Autowired
	Flux<AuthCreateEvent> authCreateEventFlux;

	@Autowired
	Flux<AuthExpireEvent> authExpireEventFlux;

	@MockBean
	UserService userService;

	@MockBean
	AuthenticationRepository authRepo;

	@Test
	void signIn() {
		User mockUser = MockBuilder.getMockUser("mock@mock.com");
		Mockito.when(userService.user(any()))
		       .thenReturn(Mono.just(mockUser));
		Mockito.when(authRepo.save(any())).thenAnswer(invocationOnMock ->
				Mono.just(invocationOnMock.getArgument(0)));

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

	@Test
	void refresh() {
		User mockUser = MockBuilder.getMockUser("mock@mock.com");
		Authentication mockAuth = jwtService.signIn(mockUser);
		Mockito.when(authRepo.findByRefreshTokenAndAccessToken(mockAuth.getRefreshToken(), mockAuth.getAccessToken()))
		       .thenReturn(Mono.just(mockAuth));
		Mockito.when(authRepo.delete(mockAuth)).thenReturn(Mono.empty());
		Mockito.when(authRepo.save(any())).thenAnswer(t -> Mono.just(t.getArgument(0)));
		Mockito.when(userService.user("mock@mock.com")).thenReturn(Mono.just(mockUser));

		sessionService.refresh(mockAuth).log()
				.as(StepVerifier::create)
				.expectNextMatches(newAuth -> !mockAuth.getAccessToken().equals(newAuth.getAccessToken()) &&
						!mockAuth.getRefreshToken().equals(newAuth.getRefreshToken()) &&
						jwtService.parseEmail(newAuth.getAccessToken()).equals("mock@mock.com"))
				.verifyComplete();

		//todo: verify flux event published, with mockito verify
	}
}