package com.grasstudy.member.repository;

import com.grasstudy.member.TestRepositoryConfiguration;
import com.grasstudy.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Test
	void uniqueSave() {

		Member mockMember = Member.builder()
		                          .userId("testUser")
		                          .name("test-user")
		                          .password("12345")
		                          .email("test@test.com")
		                          .build();

		StepVerifier.create(memberRepository.uniqueSave(mockMember))
		            .expectNext(mockMember)
		            .verifyComplete();

		StepVerifier.create(memberRepository.findById(1l).log())
		            .expectNext(mockMember)
		            .verifyComplete();
	}
}