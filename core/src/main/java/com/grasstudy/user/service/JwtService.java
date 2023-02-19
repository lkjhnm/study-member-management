package com.grasstudy.user.service;


import com.grasstudy.common.session.PkiBasedValidator;
import com.grasstudy.common.session.event.SigningKeyPublisher;
import com.grasstudy.common.session.event.scheme.SigningKeyCreateEvent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
@RequiredArgsConstructor
public class JwtService {

	static final String CLAIM_KEY_USERID = "userId";
	private final PkiBasedValidator<Claims> jwtValidator;
	private final SigningKeyPublisher<Flux<SigningKeyCreateEvent>> signingKeyPublisher;
	private SignKey signKey;

	@PostConstruct
	private void init() {
		this.signKey = new SignKey(UUID.randomUUID().toString(), Keys.keyPairFor(SignatureAlgorithm.ES256));
		signingKeyPublisher.publish(SigningKeyCreateEvent.builder()
		                                                 .kid(signKey.kid)
		                                                 .algorithm(signKey.publicKey.getAlgorithm())
		                                                 .publicKey(signKey.publicKey.getEncoded())
		                                                 .build());

		// todo: publish하면 setting 안되나?
		jwtValidator.setSigningKeys(Map.of(signKey.kid, signKey.publicKey));
	}

	public PublicKey getPublicKey() {
		return this.signKey.publicKey;
	}

	public String sign(String userId) {
		return Jwts.builder()
		           .setHeaderParam("kid", this.signKey.kid)
		           .setClaims(Map.of(CLAIM_KEY_USERID, userId))
		           .signWith(this.signKey.privateKey)
		           .setExpiration(toDate(LocalDateTime.now().plusHours(1)))
		           .setIssuedAt(new Date())
		           .compact();
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public String parseUserId(String accessToken) {
		return jwtValidator.validate(accessToken).get(CLAIM_KEY_USERID).toString();
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
