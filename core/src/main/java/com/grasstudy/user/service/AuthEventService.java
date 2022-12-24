package com.grasstudy.user.service;

import com.grasstudy.user.event.scheme.AuthCreateEvent;
import com.grasstudy.user.event.scheme.AuthExpireEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class AuthEventService {

	private final Flux<AuthCreateEvent> authCreateEventFlux;
	private final Flux<AuthExpireEvent> authExpireEventFlux;

	@PostConstruct
	public void init() {
		publishCreatedAuth(authCreateEventFlux);
		publishExpiredAuth(authExpireEventFlux);
	}

	//todo: publish to event broker
	private void publishCreatedAuth(Flux<AuthCreateEvent> authCreateEventFlux) {
		authCreateEventFlux
				.log()
				.subscribe();
	}

	//todo: publish to event broker
	private void publishExpiredAuth(Flux<AuthExpireEvent> authExpireEventFlux) {
		authExpireEventFlux
				.log()
				.subscribe();
	}
}
