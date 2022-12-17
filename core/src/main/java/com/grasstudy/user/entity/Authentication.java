package com.grasstudy.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authentication implements Persistable<String> {

	@Id
	@JsonIgnore
	private String id;

	private String refreshToken;

	private String accessToken;

	@Override
	@JsonIgnore
	public boolean isNew() {
		boolean isNew = Objects.isNull(this.id);
		this.id = isNew ? UUID.randomUUID().toString() : this.id;
		return isNew;
	}
}
