package com.grasstudy.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {

	private String userId;
	private String password;
}
