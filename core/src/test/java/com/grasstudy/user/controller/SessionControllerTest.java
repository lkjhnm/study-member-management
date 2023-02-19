package com.grasstudy.user.controller;

import com.grasstudy.user.dto.SignInRequest;
import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.service.SessionService;
import com.grasstudy.user.support.MockData;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;

@WebFluxTest(SessionController.class)
class SessionControllerTest extends ControllerTestBase {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	SessionService sessionService;

	@SpyBean
	SessionController sessionController;

	@Test
	void sign_in() {
		Authentication mockToken = Authentication.builder()
		                                         .refreshToken("test-refresh-token")
		                                         .accessToken(Jwts.builder().compact())
		                                         .build();
		SignInRequest mockParam = MockData.signInRequest();
		Mockito.when(sessionService.signIn(eq(mockParam.getUserId()), eq(mockParam.getPassword())))
		       .thenReturn(Mono.just(mockToken));
		webTestClient.post()
		             .uri("/session/sign-in")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(mockParam))
		             .exchange()
		             .expectStatus().is2xxSuccessful()
		             .expectBody()
		             .consumeWith(result -> logger.info("{}", result))
		             .jsonPath("$.refreshToken").isEqualTo(mockToken.getRefreshToken())
		             .jsonPath("$.accessToken").isEqualTo(mockToken.getAccessToken())
		             .jsonPath("$.id").doesNotHaveJsonPath();

		Mockito.verify(sessionController).signIn(eq(mockParam));
	}

	@Test
	void sign_in_failure() {
		SignInRequest mockParam = MockData.signInRequest();
		Mockito.when(sessionService.signIn(anyString(), anyString()))
		       .thenReturn(Mono.error(RuntimeException::new));

		webTestClient.post()
		             .uri("/session/sign-in")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(mockParam))
		             .exchange()
		             .expectStatus().isEqualTo(401);
		Mockito.verify(sessionController).signIn(eq(mockParam));
	}

	@Test
	void refresh() {
		Authentication expiredAuth = MockData.expiredAuth();
		Authentication newAuth = MockData.auth();
		Mockito.when(sessionService.refresh(expiredAuth)).thenReturn(Mono.just(newAuth));

		webTestClient.post()
		             .uri("/session/refresh")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(expiredAuth))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(result -> logger.info("{}", result))
		             .jsonPath("$.refreshToken").isEqualTo(newAuth.getRefreshToken())
		             .jsonPath("$.accessToken").isEqualTo(newAuth.getAccessToken())
		             .jsonPath("$.id").doesNotHaveJsonPath();

		Mockito.verify(sessionController).refresh(eq(expiredAuth));
	}

	@Test
	void refresh_failure() {
		Authentication expiredAuth = MockData.expiredAuth();
		Mockito.when(sessionService.refresh(expiredAuth))
		       .thenReturn(Mono.error(RuntimeException::new));

		webTestClient.post()
		             .uri("/session/refresh")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(expiredAuth))
		             .exchange()
		             .expectStatus().isEqualTo(401);
	}
}