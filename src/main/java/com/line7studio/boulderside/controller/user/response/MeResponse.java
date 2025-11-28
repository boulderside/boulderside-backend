package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.common.security.details.CustomUserDetails;

import lombok.Builder;

@Builder
public record MeResponse(String email, String nickname, String profileImageUrl) {
	public static MeResponse from(CustomUserDetails user) {
		return MeResponse.builder()
			.email(user.getEmail())
			.nickname(user.getNickname())
			.profileImageUrl(user.getProfileImageUrl())
			.build();
	}
}
