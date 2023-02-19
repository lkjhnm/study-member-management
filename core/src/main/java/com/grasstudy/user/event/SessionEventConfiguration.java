package com.grasstudy.user.event;

import com.grasstudy.common.session.PkiBasedValidator;
import com.grasstudy.common.session.event.ReactorSigningKeyPublisher;
import com.grasstudy.common.session.event.ReactorSigningKeySubscriber;
import com.grasstudy.common.session.event.SigningKeyPublisher;
import com.grasstudy.common.session.event.scheme.SigningKeyCreateEvent;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class SessionEventConfiguration {

	@Bean
	public Consumer<Flux<SigningKeyCreateEvent>> keyCreateEventSubscriber(PkiBasedValidator<Claims> jwtValidator) {
		return new ReactorSigningKeySubscriber().subscriber(jwtValidator);
	}

	@Bean
	public SigningKeyPublisher<Flux<SigningKeyCreateEvent>> signingKeyPublisher() {
		return new ReactorSigningKeyPublisher();
	}

	@Bean
	public Supplier<Flux<SigningKeyCreateEvent>> keyCreateEventPublisher(
			SigningKeyPublisher<Flux<SigningKeyCreateEvent>> signingKeyPublisher) {
		return signingKeyPublisher.publisher();
	}
}
