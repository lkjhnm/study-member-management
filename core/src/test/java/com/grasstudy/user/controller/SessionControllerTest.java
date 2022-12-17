package com.grasstudy.user.controller;

import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.service.SessionService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@WebFluxTest(SessionController.class)
class SessionControllerTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	SessionService sessionService;

	@Test
	void sign_in() {
		Authentication mockToken = Authentication.builder()
		                                         .refreshToken("test-refresh-token")
		                                         .accessToken(Jwts.builder().compact())
		                                         .build();
		Mockito.when(sessionService.signIn(anyString(), anyString()))
		       .thenReturn(Mono.just(mockToken));

		webTestClient.post()
		             .uri("/session/sign-in")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue("{\n" +
				             "  \"email\" : \"mock@mock.com\",\n" +
				             "  \"password\" : \"12345!@\"\n" +
				             "}"))
		             .exchange()
		             .expectStatus().is2xxSuccessful()
		             .expectBody()
		             .consumeWith(result -> logger.info("{}", result))
		             .jsonPath("$.refreshToken").isEqualTo(mockToken.getRefreshToken())
		             .jsonPath("$.accessToken").isEqualTo(mockToken.getAccessToken())
		             .jsonPath("$.id").doesNotHaveJsonPath();
	}

	@Test
	void sign_in_failure() {
		Mockito.when(sessionService.signIn(anyString(), anyString()))
		       .thenReturn(Mono.error(RuntimeException::new));

		webTestClient.post()
		             .uri("/session/sign-in")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue("{\n" +
				             "  \"email\" : \"mock@mock.com\",\n" +
				             "  \"password\" : \"12345!@\"\n" +
				             "}"))
		             .exchange()
		             .expectStatus().isEqualTo(401);
	}
}