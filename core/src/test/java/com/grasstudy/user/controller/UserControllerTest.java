package com.grasstudy.user.controller;

import com.grasstudy.user.WithMockUser;
import com.grasstudy.user.entity.User;
import com.grasstudy.user.service.JwtService;
import com.grasstudy.user.service.UserService;
import com.grasstudy.user.support.MockData;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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

import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;

@WebFluxTest(UserController.class)
class UserControllerTest extends ControllerTestBase {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	WebTestClient webTestClient;

	@SpyBean
	UserController userController;

	@MockBean
	UserService userService;

	@MockBean
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

		Mockito.verify(userController).signup(any(User.class));
	}

	@Test
	void check() {
		Mockito.when(userService.user(any(String.class)))
		       .thenAnswer(v -> Objects.equals(v.getArgument(0), "fail_test") ?
				       Mono.just(MockData.getMockUser("mock-id")) : Mono.empty());

		webTestClient.get()
		             .uri("/user/check/mock-id")
		             .exchange()
		             .expectStatus().is2xxSuccessful();
		Mockito.verify(userController).check(eq("mock-id"));

		webTestClient.get()
		             .uri("/user/check/fail_test")
		             .exchange()
		             .expectStatus().is4xxClientError();
		Mockito.verify(userController).check(eq("fail_test"));
	}

	@Test
	@WithMockUser
	void me() {
		User mockUser = MockData.getMockUser("mock@mock.com");
		Mockito.when(userService.user(eq("mock-user"))).thenReturn(Mono.just(mockUser));

		webTestClient.get()
		             .uri("/user")
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(result -> logger.info("{}", result))
		             .jsonPath("$.email").isEqualTo(mockUser.getEmail())
		             .jsonPath("$.nickname").isEqualTo(mockUser.getNickname())
		             .jsonPath("$.password").doesNotHaveJsonPath();

		Mockito.verify(userController).me(notNull());
	}

	@Test
	void unauthorized_request() {
		webTestClient.get()
		             .uri("/user")
		             .exchange()
		             .expectStatus().isUnauthorized();
	}
}