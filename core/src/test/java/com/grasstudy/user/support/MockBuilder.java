package com.grasstudy.user.support;

import com.grasstudy.user.entity.Authentication;
import com.grasstudy.user.entity.User;

import java.util.UUID;

public class MockBuilder {
	public static User getMockUser(String email) {
		return User.builder()
		           .email(email)
		           .nickname(String.format("nickname-%s", UUID.randomUUID()))
		           .password(String.format("password-%s", UUID.randomUUID()))
		           .build();
	}

	public static Authentication auth() {
		return Authentication.builder()
		                     .accessToken(UUID.randomUUID().toString())
		                     .refreshToken(UUID.randomUUID().toString())
		                     .build();
	}
}
