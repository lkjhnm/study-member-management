package com.grasstudy.user.service;

import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.entity.User;
import com.grasstudy.user.event.AuthEventPublisher;
import com.grasstudy.user.event.scheme.AuthCreateEvent;
import com.grasstudy.user.event.scheme.AuthEvent;
import com.grasstudy.user.repository.AuthenticationRepository;
import com.grasstudy.user.support.MockData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@Import({SessionService.class})
class SessionServiceTest {

	@Autowired
	SessionService sessionService;

	@MockBean
	JwtService jwtService;

	@MockBean
	AuthEventPublisher authEventPublisher;

	@MockBean
	UserService userService;

	@MockBean
	AuthenticationRepository authRepo;

	@Test
	void signIn() {
		User mockUser = MockData.getMockUser("mock-user");
		Mockito.when(userService.user(eq(mockUser.getUserId()))).thenReturn(Mono.just(mockUser));
		Mockito.when(authRepo.save(any(Authentication.class))).thenAnswer(invocationOnMock ->
				Mono.just(invocationOnMock.getArgument(0)));
		Mockito.when(jwtService.sign("mock-user")).thenReturn("mock-access-token");

		sessionService.signIn(mockUser.getUserId(), mockUser.getPassword()).log()
		              .as(StepVerifier::create)
		              .expectNextCount(1)
		              .verifyComplete();

		Mockito.verify(authEventPublisher).publishEvent(any(AuthCreateEvent.class));
	}

	@Test
	void sign_fail() {
		User mockUser = MockData.getMockUser("mock-user");
		Mockito.when(userService.user(eq(mockUser.getUserId()))).thenReturn(Mono.just(mockUser));

		sessionService.signIn(mockUser.getUserId(), "failure_password").log()
		              .as(StepVerifier::create)
		              .expectError(RuntimeException.class)
		              .verify();
	}

	@Test
	void refresh() {
		User mockUser = MockData.getMockUser("mock-user");
		Authentication mockAuth = MockData.auth();
		Mockito.when(authRepo.findByRefreshTokenAndAccessToken(mockAuth.getRefreshToken(), mockAuth.getAccessToken()))
		       .thenReturn(Mono.just(mockAuth));
		Mockito.when(authRepo.deleteByRefreshTokenAndAccessToken(mockAuth.getRefreshToken(), mockAuth.getAccessToken()))
		       .thenReturn(Mono.empty());
		Mockito.when(authRepo.save(any(Authentication.class))).thenAnswer(t -> Mono.just(t.getArgument(0)));
		Mockito.when(jwtService.parseUserId(mockAuth.getAccessToken())).thenReturn(mockUser.getUserId());
		Mockito.when(userService.user(mockUser.getUserId())).thenReturn(Mono.just(mockUser));

		sessionService.refresh(mockAuth).log()
		              .as(StepVerifier::create)
		              .expectNextMatches(newAuth -> !newAuth.equals(mockAuth))
		              .verifyComplete();

		Mockito.verify(authEventPublisher, Mockito.times(2))
		       .publishEvent(any(AuthEvent.class));
	}

	@Test
	void refresh_fail() {
		Authentication mockAuth = MockData.auth();
		Mockito.when(authRepo.findByRefreshTokenAndAccessToken(mockAuth.getRefreshToken(), mockAuth.getAccessToken()))
		       .thenReturn(Mono.empty());

		sessionService.refresh(mockAuth).log()
		              .as(StepVerifier::create)
		              .expectError().verify();

		Mockito.verify(authEventPublisher, Mockito.times(0))
		       .publishEvent(any());
	}
}