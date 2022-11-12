package com.grasstudy.user.controller;

import com.grasstudy.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

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
		Mockito.when(userService.signup(any())).thenReturn(Mono.just(ResponseEntity.ok().build()));

		webTestClient.post()
		             .uri("/user/signup")
		             .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		             .body(BodyInserters.fromValue("{\n" +
				             "  \"userId\" : \"mock-id\",\n" +
				             "  \"password\" : \"12345!@\",\n" +
				             "  \"email\" : \"mock@mock.com\",\n" +
				             "  \"interestTags\" : [\"java\", \"msa\", \"love\"]\n" +
				             "}"))
		             .exchange()
		             .expectStatus().is2xxSuccessful();
	}
}