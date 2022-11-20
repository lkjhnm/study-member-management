package com.grasstudy.user.service;

import com.grasstudy.user.support.MockBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(JwtService.class)
class JwtServiceTest {

	@Autowired
	JwtService jwtService;

	@Test
	void signIn() {
		String jws = jwtService.signIn(MockBuilder.getMockUser("mock@mock.com"));

		Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(jwtService.getPublicKey())
		                            .build().parseClaimsJws(jws);

		Assertions.assertThat(claimsJws.getHeader().getKeyId()).isNotNull();
		Assertions.assertThat(claimsJws.getHeader().getAlgorithm()).isEqualTo("ES256");
		Assertions.assertThat(claimsJws.getBody().get("email")).isEqualTo("mock@mock.com");
	}
}
