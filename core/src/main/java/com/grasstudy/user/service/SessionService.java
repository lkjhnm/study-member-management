package com.grasstudy.user.service;

import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.entity.User;
import com.grasstudy.user.event.AuthEventPublisher;
import com.grasstudy.user.event.scheme.AuthCreateEvent;
import com.grasstudy.user.event.scheme.AuthExpireEvent;
import com.grasstudy.user.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

@Service
@RequiredArgsConstructor
public class SessionService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationRepository authRepo;
	private final AuthEventPublisher authEventPublisher;

	public Mono<Authentication> signIn(String email, String password) {
		return userService.user(email)
		                  .filter(v -> this.checkPassword(v.getPassword(), password))
		                  .flatMap(this::signIn)
		                  .switchIfEmpty(Mono.error(RuntimeException::new));    //todo: 로그인 실패 예외 정의
	}

	// todo : password encrypt 적용
	private boolean checkPassword(String origin, String given) {
		return origin.equals(given);
	}

	public Mono<Authentication> refresh(Authentication auth) {
		return authRepo.findByRefreshTokenAndAccessToken(auth.getRefreshToken(), auth.getAccessToken())
		               .map(this::validate)
		               .doFinally(v -> this.publishAuthExpired(v, auth))
		               .flatMap(this::getUser)
		               .flatMap(this::signIn)
		               .switchIfEmpty(Mono.error(RuntimeException::new));
	}

	private Authentication validate(Authentication authentication) {
		if (authentication.isExpired()) {
			throw new RuntimeException("This authentication is expired");
		}
		return authentication;
	}

	private Mono<User> getUser(Authentication auth) {
		return userService.user(jwtService.parseEmail(auth.getAccessToken()));
	}

	private Mono<Authentication> signIn(User user) {
		return authRepo.save(jwtService.signIn(user))
		           .doOnSuccess(this::publishAuthCreated);
	}

	private void publishAuthExpired(SignalType signalType, Authentication auth) {
		if (signalType == SignalType.ON_NEXT ||
				signalType == SignalType.ON_COMPLETE ||
				signalType == SignalType.ON_ERROR) {
			authRepo.deleteByRefreshTokenAndAccessToken(auth.getRefreshToken(), auth.getAccessToken())
			        .doOnSuccess(unused ->
					        authEventPublisher.publishEvent(
							        AuthExpireEvent.builder().auth(auth).build()))
			        .subscribe();
		}
	}

	private void publishAuthCreated(Authentication auth) {
		authEventPublisher.publishEvent(AuthCreateEvent.builder().auth(auth).build());
	}
}
