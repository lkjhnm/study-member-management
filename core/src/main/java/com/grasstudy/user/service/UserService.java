package com.grasstudy.user.service;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public Mono<ResponseEntity<Void>> signup(User user) {
		return userRepository.save(user)
		                     .map(v -> ResponseEntity.ok().<Void>build())
		                     .onErrorReturn(ResponseEntity.internalServerError().build());
	}


}
