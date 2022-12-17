package com.grasstudy.user.service;

import com.grasstudy.user.entity.Authentication;
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
		                  .map(jwtService::signIn)
		                  .flatMap(authRepo::save)
		                  .switchIfEmpty(Mono.error(RuntimeException::new));    //todo: 로그인 실패 예외 정의
	}

	private boolean checkPassword(String origin, String given) {
		return origin.equals(given);
	}
}
