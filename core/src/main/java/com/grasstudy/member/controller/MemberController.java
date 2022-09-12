package com.grasstudy.member.controller;

import com.grasstudy.member.entity.Member;
import com.grasstudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	// 회원가입
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public Mono<ResponseEntity<Void>> signup(Member member) {
		return memberService.signup(member);
	}

	// 사용자 조회

}
