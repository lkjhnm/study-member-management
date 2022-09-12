package com.grasstudy.member.service;

import com.grasstudy.member.entity.Member;
import com.grasstudy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Mono<ResponseEntity<Void>> signup(Member member) {
		return memberRepository.uniqueSave(member)
				.map(v -> ResponseEntity.ok().<Void>build())
				.onErrorReturn(ResponseEntity.internalServerError().build());
	}


}
