package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;

import lombok.Builder;

@Builder
public record MeResponse(Long userId, String nickname, String profileImageUrl, UserRole role) {
	public static MeResponse from(CustomUserDetails user) {
		return MeResponse.builder()
			.userId(user.userId())
			.nickname(user.nickname())
			.profileImageUrl(user.profileImageUrl())
			.role(user.userRole())
			.build();
	}
}
