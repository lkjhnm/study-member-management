package com.grasstudy.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
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

	@JsonIgnore
	private LocalDateTime expiredAt;

	@Override
	@JsonIgnore
	public boolean isNew() {
		boolean isNew = Objects.isNull(this.id);
		this.id = isNew ? UUID.randomUUID().toString() : this.id;
		return isNew;
	}

	@JsonIgnore
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiredAt);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Authentication that = (Authentication) o;
		return refreshToken.equals(that.refreshToken) && accessToken.equals(that.accessToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(refreshToken, accessToken);
	}
}
