package com.grasstudy.user.controller;

import com.grasstudy.user.service.UserService;
import com.grasstudy.user.support.MockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
@WebFluxTest(UserController.class)
@Import(UserService.class)
class UserControllerTest {

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	UserService userService;


	@Test
	void signup() {
		Mockito.when(userService.signup(ArgumentMatchers.argThat(user -> Objects.nonNull(user.getUserId()))))
		       .thenAnswer(v -> Mono.just(v.getArgument(0)));

		webTestClient.post()
		             .uri("/user/signup")
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue("{\n" +
				             "  \"userId\" : \"mock-id\",\n" +
				             "  \"password\" : \"12345!@\",\n" +
				             "  \"email\" : \"mock@mock.com\",\n" +
				             "  \"interestTags\" : [\"java\", \"msa\", \"love\"]\n" +
				             "}"))
		             .exchange()
		             .expectStatus().is2xxSuccessful();
	}

	@Test
	void check() {
		Mockito.when(userService.user(ArgumentMatchers.argThat(id -> Objects.nonNull(id))))
		       .thenAnswer(v -> Objects.equals(v.getArgument(0), "fail_test") ?
				       Mono.empty() : Mono.just(MockBuilder.getMockUser("mock-id")));

		webTestClient.get()
		             .uri("/user/check/mock-id")
		             .exchange()
		             .expectStatus().is2xxSuccessful();

		webTestClient.get()
		             .uri("/user/check/fail_test")
		             .exchange()
		             .expectStatus().is4xxClientError();
	}
}