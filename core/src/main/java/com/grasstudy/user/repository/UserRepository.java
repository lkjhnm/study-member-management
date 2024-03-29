package com.grasstudy.user.repository;


import com.grasstudy.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {

	Mono<User> findByEmail(String email);

	Mono<User> findByUserId(String userId);

}
