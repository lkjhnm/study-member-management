package com.grasstudy.user.controller;

import com.grasstudy.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

	private final SessionService sessionService;

	@RequestMapping(value = "/sign-in", method = RequestMethod.POST)
	public Mono<ResponseEntity<String>> signIn(@RequestBody Map<String, String> params) {
		return sessionService.signIn(params.get("email"), params.get("password"))
				.map(ResponseEntity::ok)
				.onErrorReturn(ResponseEntity.status(401).build());
	}
}
