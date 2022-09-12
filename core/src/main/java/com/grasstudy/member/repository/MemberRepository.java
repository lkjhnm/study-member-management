package com.grasstudy.member.repository;


import com.grasstudy.member.entity.Member;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {

	Mono<Member> findByUserId(String userId);

	default Mono<Member> uniqueSave(Member member) {
		return findByUserId(member.getUserId())
				.hasElement()
				.flatMap(v -> v.booleanValue() ?
						Mono.error(new RuntimeException("Member already exists!")) :
						save(member));
	}
}
