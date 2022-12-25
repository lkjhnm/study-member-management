package com.grasstudy.user.controller;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.service.JwtService;
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
	private final JwtService jwtService;

	// 회원가입
	@RequestMapping(method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> signup(@RequestBody User user) {
		return userService.signup(user)
		                  .map(v -> ResponseEntity.ok().<Void>build())
		                  .onErrorReturn(ResponseEntity.internalServerError().build());
	}

	@RequestMapping(value = "/check/{email}", method = RequestMethod.GET)
	public Mono<ResponseEntity<Void>> check(@PathVariable String email) {
		return userService.user(email)
		                  .map(v -> ResponseEntity.status(409).<Void>build())
		                  .switchIfEmpty(Mono.just(ResponseEntity.ok().build()));
	}

	// 사용자 조회
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public Mono<ResponseEntity<User>> me(@RequestHeader String authorization) {
		return userService.user(jwtService.parseEmail(parseAccessToken(authorization)))
		                  .map(ResponseEntity::ok)
		                  .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	//todo: do it this, spring security filter module
	private String parseAccessToken(String authorization) {
		return authorization.substring(authorization.indexOf(" "));
	}
}
