package com.grasstudy.user.controller;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원가입
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> signup(@RequestBody User user) {
		return userService.signup(user)
		                  .map(v -> ResponseEntity.ok().<Void>build())
		                  .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/check/{userId}", method = RequestMethod.GET)
	public Mono<ResponseEntity<Void>> check(@PathVariable String userId) {
		return userService.user(userId)
		                  .map(v -> ResponseEntity.status(409).<Void>build())
		                  .switchIfEmpty(Mono.just(ResponseEntity.ok().build()));
	}

	// 사용자 조회

}
