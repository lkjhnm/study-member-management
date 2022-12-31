package com.grasstudy.user.controller;

import com.grasstudy.user.entity.User;
import com.grasstudy.user.service.JwtService;
import com.grasstudy.user.service.UserService;
import com.grasstudy.user.support.MockBuilder;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@WebFluxTest(UserController.class)
@Import({UserService.class, JwtService.class})
class UserControllerTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	UserService userService;

	@SpyBean
	JwtService jwtService;

	@Test
	void signup() {
		Mockito.when(userService.signup(ArgumentMatchers.argThat(user -> Objects.nonNull(user.getEmail()))))
		       .thenAnswer(v -> Mono.just(v.getArgument(0)));

		webTestClient.post()
		             .uri("/user")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue("{\n" +
				             "  \"email\" : \"mock@mock.com\",\n" +
				             "  \"password\" : \"12345!@\",\n" +
				             "  \"interestTags\" : [\"java\", \"msa\", \"love\"]\n" +
				             "}"))
		             .exchange()
		             .expectStatus().is2xxSuccessful();
	}

	@Test
	void check() {
		Mockito.when(userService.user(ArgumentMatchers.argThat(id -> Objects.nonNull(id))))
		       .thenAnswer(v -> Objects.equals(v.getArgument(0), "fail_test") ?
				       Mono.just(MockBuilder.getMockUser("mock-id")) : Mono.empty());

		webTestClient.get()
		             .uri("/user/check/mock-id")
		             .exchange()
		             .expectStatus().is2xxSuccessful();

		webTestClient.get()
		             .uri("/user/check/fail_test")
		             .exchange()
		             .expectStatus().is4xxClientError();
	}

	@Test
	void user() {
		User mockUser = MockBuilder.getMockUser("mock@mock.com");
		Mockito.when(userService.user(anyString()))
		       .thenReturn(Mono.just(mockUser));
		String accessToken = jwtService.signIn(mockUser).getAccessToken();

		webTestClient.get()
		             .uri("/user")
		             .header("Authorization", String.format("Bearer %s", accessToken))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(result -> logger.info("{}", result))
		             .jsonPath("$.email").isEqualTo(mockUser.getEmail())
		             .jsonPath("$.nickname").isEqualTo(mockUser.getNickname())
		             .jsonPath("$.password").doesNotHaveJsonPath();
	}

	@Test
	void user_when_token_expired() {
		User mockUser = MockBuilder.getMockUser("mock@mock.com");
		String accessToken = jwtService.signIn(mockUser).getAccessToken();
		Mockito.doThrow(new JwtException("test exception"))
		       .when(jwtService).parseEmail(anyString());

		webTestClient.get()
		             .uri("/user")
		             .header("Authorization", String.format("Bearer %s", accessToken))
		             .exchange()
		             .expectStatus().isUnauthorized();
	}
}