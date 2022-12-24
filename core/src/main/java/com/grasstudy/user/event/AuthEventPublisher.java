package com.grasstudy.user.event;

import com.grasstudy.user.event.scheme.AuthCreateEvent;
import com.grasstudy.user.event.scheme.AuthEvent;
import com.grasstudy.user.event.scheme.AuthExpireEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class AuthEventPublisher {

	private final Flux<AuthCreateEvent> createEventFlux;
	private final Flux<AuthExpireEvent> expireEventFlux;

	private Consumer<AuthCreateEvent> createEventConsumer;
	private Consumer<AuthExpireEvent> expireEventConsumer;

	public AuthEventPublisher() {
		this.createEventFlux = Flux.create(emitter -> createEventConsumer = emitter::next);
		this.expireEventFlux = Flux.create(emitter -> expireEventConsumer = emitter::next);
	}

	public void publishEvent(AuthEvent authEvent) {
		if (authEvent instanceof AuthCreateEvent) {
			this.createEventConsumer.accept((AuthCreateEvent) authEvent);
		} else if (authEvent instanceof AuthExpireEvent) {
			this.expireEventConsumer.accept((AuthExpireEvent) authEvent);
		}
	}

	@Bean
	public Flux<AuthCreateEvent> authCreateEventFlux() {
		return this.createEventFlux;
	}

	@Bean
	public Flux<AuthExpireEvent> authExpireEventFlux() {
		return this.expireEventFlux;
	}
}
