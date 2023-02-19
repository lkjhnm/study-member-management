package com.grasstudy.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	@JsonIgnore
	private String id;
	private String userId;
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;    // todo: password encoder
	private String nickname;
	private List<String> interestTags;
	@JsonIgnore
	private String fcmToken;

	@Override
	@JsonIgnore
	public boolean isNew() {
		boolean isNew = Objects.isNull(this.id);
		this.id = isNew ? UUID.randomUUID().toString() : this.id;
		return isNew;
	}
}
