package com.line7studio.boulderside.domain.feature.user.entity;

import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.feature.user.enums.AuthProviderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user_credentials",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_user_credential_provider",
			columnNames = {"provider_type", "provider_user_id"})
	})
public class UserCredential extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false, unique = true)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider_type", nullable = false)
	private AuthProviderType providerType;

	@Column(name = "provider_user_id", nullable = false)
	private String providerUserId;

	@Column(name = "provider_email")
	private String providerEmail;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	public static UserCredential create(Long userId, AuthProviderType providerType, String providerUserId,
		String refreshToken) {
		return UserCredential.builder()
			.userId(userId)
			.providerType(providerType)
			.providerUserId(providerUserId)
			.refreshToken(refreshToken)
			.lastLoginAt(LocalDateTime.now())
			.build();
	}

	public void updateLoginSession(String refreshToken) {
		this.refreshToken = refreshToken;
		this.lastLoginAt = LocalDateTime.now();
	}
}
