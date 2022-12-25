package com.grasstudy.user.repository;

import com.grasstudy.user.entity.Authentication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AuthenticationRepository extends ReactiveCrudRepository<Authentication, String> {

	Mono<Authentication> findByRefreshTokenAndAccessToken(String refreshToken, String accessToken);

	Mono<Void> deleteByRefreshTokenAndAccessToken(String refreshToken, String accessToken);
}
