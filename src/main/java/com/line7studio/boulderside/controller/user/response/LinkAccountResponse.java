package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserSex;

public record LinkAccountResponse(
	String nickname,
	String phone,
	UserRole userRole,
	UserSex userSex,
	Level userLevel,
	String name
) {
	public static LinkAccountResponse from(User user) {
		return new LinkAccountResponse(
			user.getNickname(),
			user.getPhone(),
			user.getUserRole(),
			user.getUserSex(),
			user.getUserLevel(),
			user.getName()
		);
	}
}
