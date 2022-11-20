package com.grasstudy.user.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Table(name = "G_USER")
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Persistable<String> {

	@Id
	private String id;
	private String email;
	private String password;    // todo: password encoder
	private String nickname;
	private List<String> interestTags;
	private String fcmToken;

	@Override
	public boolean isNew() {
		boolean isNew = Objects.isNull(this.id);
		this.id = isNew ? UUID.randomUUID().toString() : this.id;
		return isNew;
	}
}
