package com.grasstudy.user.controller;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원가입
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> signup(User user) {
		return userService.signup(user);
	}

	// 사용자 조회

}
