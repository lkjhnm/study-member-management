package com.grasstudy.user.controller;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원가입
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> signup(@RequestBody User user) {
		return userService.signup(user);
	}

	// 사용자 조회

}
