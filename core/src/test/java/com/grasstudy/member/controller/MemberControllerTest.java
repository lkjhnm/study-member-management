package com.grasstudy.member.controller;

import com.grasstudy.member.entity.Member;
import com.grasstudy.member.repository.MemberRepository;
import com.grasstudy.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(MemberController.class)
@Import(MemberService.class)
class MemberControllerTest {

	@MockBean
	MemberRepository memberRepository;

	@Autowired
	WebTestClient webTestClient;

	@Test
	void signup() {
		Mockito.when(memberRepository.uniqueSave(Mockito.any()))
		       .thenReturn(Mono.just(Member.builder()
		                                   .build()));

		webTestClient.post()
		             .uri("/signup")
		             .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		             .body(BodyInserters.fromFormData("name", "username")
		                                .with("userId", "testUser")
		                                .with("password", "12345")
		                                .with("email", "test@test.com")
		                                .with("interestTags", "java")
		                                .with("interestTags", "msa")
		                                .with("interestTags", "member")
		             )
		             .exchange()
		             .expectStatus().is2xxSuccessful();
	}
}