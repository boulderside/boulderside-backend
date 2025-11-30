package com.line7studio.boulderside.controller.user.request;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import com.line7studio.boulderside.domain.feature.user.enums.UserSex;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
	@NotBlank String nickname,
	@NotBlank String phoneNumber,
	UserRole userRole,
	UserSex userSex,
	Level userLevel,
	String name,
	@NotBlank String email,
	@NotBlank String password
) {
}
