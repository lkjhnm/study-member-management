package com.grasstudy.member.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@ToString
public class Member {

	@Id
	private Long id;
	private String userId;
	private String password;    // todo: password encoder
	private String name;
	private String email;
	private List<String> interestTags;
	private String fcmToken;
}
