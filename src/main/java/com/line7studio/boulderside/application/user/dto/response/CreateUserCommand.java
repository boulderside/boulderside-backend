package com.line7studio.boulderside.application.user.dto.response;

import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import lombok.Builder;

@Builder
public record CreateUserCommand(
	String nickname,
	UserRole userRole
) {
}
