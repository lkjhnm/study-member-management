package com.grasstudy.user.event.scheme;

import com.grasstudy.user.entity.Authentication;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AuthCreateEvent implements AuthEvent {

	private Authentication auth;
}
