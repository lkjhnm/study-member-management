package com.grasstudy.user.service;


import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.entity.User;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

	static final String CLAIM_KEY_EMAIL = "email";
	private SignKey signKey;
	private JwtParser jwtParser;

	@PostConstruct
	private void init() {
		this.signKey = new SignKey(UUID.randomUUID().toString(), Keys.keyPairFor(SignatureAlgorithm.ES256));
		this.jwtParser = Jwts.parserBuilder().setSigningKey(this.getPublicKey()).build();
	}

	public PublicKey getPublicKey() {
		return this.signKey.publicKey;
	}

	public Authentication signIn(User user) {
		return Authentication.builder()
		                     .refreshToken(UUID.randomUUID().toString())
		                     .accessToken(Jwts.builder()
		                                      .setHeaderParam("kid", this.signKey.kid)
		                                      .setClaims(Map.of(CLAIM_KEY_EMAIL, user.getEmail()))
		                                      .signWith(this.signKey.privateKey)
		                                      .setExpiration(toDate(LocalDateTime.now().plusHours(1)))
		                                      .setIssuedAt(new Date())
		                                      .compact())
		                     .expiredAt(LocalDateTime.now().plusHours(12))
		                     .build();
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public String parseEmail(String accessToken) {
		return jwtParser.parseClaimsJws(accessToken)
		                .getBody().get(CLAIM_KEY_EMAIL).toString();
	}

	private static class SignKey {
		public SignKey(String kid, KeyPair keyPair) {
			this.kid = kid;
			this.privateKey = keyPair.getPrivate();
			this.publicKey = keyPair.getPublic();
		}

		String kid;
		PrivateKey privateKey;
		PublicKey publicKey;
	}
}
