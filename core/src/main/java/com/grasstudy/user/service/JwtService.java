package com.grasstudy.user.service;


import com.grasstudy.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

	private SignKey signKey;

	@PostConstruct
	private void init() {
		this.signKey = new SignKey(UUID.randomUUID().toString(), Keys.keyPairFor(SignatureAlgorithm.ES256));
	}

	public PublicKey getPublicKey() {
		return this.signKey.publicKey;
	}

	public String signIn(User user) {
		return Jwts.builder()
		           .setHeaderParam("kid", this.signKey.kid)
		           .setClaims(Map.of("email", user.getEmail()))
		           .signWith(this.signKey.privateKey)
		           .compact();
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
