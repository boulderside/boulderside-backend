package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserSex;

import lombok.Builder;

@Builder
public record LinkAccountResponse(
	String nickname,
	String phone,
	UserRole userRole,
	UserSex userSex,
	Level userLevel,
	String name
) {
	public static LinkAccountResponse from(User user) {
		return LinkAccountResponse.builder()
			.nickname(user.getNickname())
			.phone(user.getPhone())
			.userRole(user.getUserRole())
			.userSex(user.getUserSex())
			.userLevel(user.getUserLevel())
			.name(user.getName())
			.build();
	}
}
