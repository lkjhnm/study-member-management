package com.grasstudy.user.controller;

import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {

	@ExceptionHandler(JwtException.class)
	ResponseEntity<?> sessionHandler() {
		return ResponseEntity.status(401).build();
	}
}
