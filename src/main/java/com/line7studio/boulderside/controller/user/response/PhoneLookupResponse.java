package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import com.line7studio.boulderside.domain.feature.user.enums.UserSex;

import lombok.Builder;

@Builder
public record PhoneLookupResponse(
	boolean exists,
	String nickname,
	String phone,
	UserRole userRole,
	UserSex userSex,
	Level userLevel,
	String name
) {
	public static PhoneLookupResponse from(User user) {
		if (user == null) {
			return PhoneLookupResponse.builder()
				.exists(false)
				.build();
		}
		return PhoneLookupResponse.builder()
			.exists(true)
			.nickname(user.getNickname())
			.phone(user.getPhone())
			.userRole(user.getUserRole())
			.userSex(user.getUserSex())
			.userLevel(user.getUserLevel())
			.name(user.getName())
			.build();
	}

	public static PhoneLookupResponse notExists() {
		return PhoneLookupResponse.builder()
			.exists(false)
			.build();
	}
}
