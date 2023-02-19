package com.grasstudy.user.controller;

import com.grasstudy.user.dto.SignInRequest;
import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

	private final SessionService sessionService;

	@ResponseBody
	@RequestMapping(value = "/sign-in", method = RequestMethod.POST)
	public Mono<ResponseEntity<Authentication>> signIn(@RequestBody SignInRequest signInRequest) {
		return sessionService.signIn(signInRequest.getUserId(), signInRequest.getPassword())
		                     .map(ResponseEntity::ok)
		                     .onErrorReturn(ResponseEntity.status(401).build());
	}

	@ResponseBody
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public Mono<ResponseEntity<Authentication>> refresh(@RequestBody Authentication auth) {
		return sessionService.refresh(auth)
		                     .map(ResponseEntity::ok)
		                     .onErrorReturn(ResponseEntity.status(401).build());
	}
}
