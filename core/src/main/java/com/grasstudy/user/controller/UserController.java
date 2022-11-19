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
	}

	// 사용자 조회

}
