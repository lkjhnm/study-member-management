package com.grasstudy.member.repository;

import com.grasstudy.member.entity.Member;
import org.reactivestreams.Publisher;
import org.springframework.core.annotation.Order;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class MemberEntityCallback implements BeforeConvertCallback<Member> {

	@Override
	public Publisher<Member> onBeforeConvert(Member entity, SqlIdentifier table) {
		if (entity.getId() == null) {
			entity.setId(1l);   //todo: auto generated number
		}
		return Mono.just(entity);
	}
}
