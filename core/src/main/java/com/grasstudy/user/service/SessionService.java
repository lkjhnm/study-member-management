package com.grasstudy.user.service;

import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.entity.User;
import com.grasstudy.user.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SessionService {

	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationRepository authRepo;

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
		               .doOnNext(authRepo::delete)
		               .map(Authentication::getAccessToken)
		               .map(jwtService::parseEmail)
		               .flatMap(userService::user)
		               .flatMap(this::signIn)
		               .switchIfEmpty(Mono.error(RuntimeException::new));
	}

	private Mono<Authentication> signIn(User user) {
		return authRepo.save(jwtService.signIn(user));
	}
}
