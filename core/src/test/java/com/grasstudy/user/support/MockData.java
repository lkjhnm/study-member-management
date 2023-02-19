package com.grasstudy.user.support;

import com.grasstudy.user.dto.SignInRequest;
import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class MockData {
	public static User getMockUser(String userId) {
		return User.builder()
		           .userId(userId)
		           .email("mock-user@mock.com")
		           .nickname(String.format("nickname-%s", UUID.randomUUID()))
		           .password(String.format("password-%s", UUID.randomUUID()))
		           .build();
	}

	public static Authentication auth() {
		return auth(LocalDateTime.now().plusHours(12));
	}

	public static Authentication expiredAuth() {
		return auth(LocalDateTime.now().minusHours(12));
	}

	private static Authentication auth(LocalDateTime expiredAt) {
		return Authentication.builder()
		                     .accessToken(UUID.randomUUID().toString())
		                     .refreshToken(UUID.randomUUID().toString())
		                     .expiredAt(expiredAt)
		                     .build();
	}

	public static SignInRequest signInRequest() {
		return SignInRequest.builder()
		                    .userId("mock-user")
		                    .password("12345!@")
		                    .build();
	}
}
