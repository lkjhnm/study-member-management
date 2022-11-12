package com.grasstudy.user.support;

import com.grasstudy.user.entity.User;

import java.util.UUID;

public class MockBuilder {
	public static User getMockUser(String userId) {
		return User.builder()
		           .userId(userId)
		           .name(String.format("user name-%s", UUID.randomUUID()))
		           .password(String.format("password-%s", UUID.randomUUID()))
		           .email(String.format("user mail-%s", UUID.randomUUID()))
		           .build();
	}
}
