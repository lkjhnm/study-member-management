package com.grasstudy.user.service;

import com.grasstudy.common.session.PkiBasedJwtValidator;
import com.grasstudy.common.session.event.SigningKeyPublisher;
import com.grasstudy.common.session.event.scheme.SigningKeyCreateEvent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@Import({JwtService.class, PkiBasedJwtValidator.class})
class JwtServiceTest {

	@Autowired
	JwtService jwtService;

	@Autowired
	PkiBasedJwtValidator pkiBasedJwtValidator;

	@MockBean
	SigningKeyPublisher<Flux<SigningKeyCreateEvent>> signingKeyPublisher;

	@Test
	void sign() {
		String jwt = jwtService.sign("test-user");
		Claims claims = pkiBasedJwtValidator.validate(jwt);
		Assertions.assertThat(claims.get("userId")).isEqualTo("test-user");
	}
}
