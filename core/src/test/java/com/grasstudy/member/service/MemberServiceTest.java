package com.grasstudy.member.service;

import com.grasstudy.member.entity.Member;
import com.grasstudy.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(MemberRepository.class)
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Test
	void signup() {
		Member mockMember = Member.builder()
		                     .userId("testUser")
		                     .password("12345")
		                     .email("test@test.com")
		                     .interestTags(List.of("java", "unit-test"))
		                     .build();

		StepVerifier.create(memberService.signup(mockMember))
				.expectNext(ResponseEntity.ok().<Void>build());
	}
}