package com.line7studio.boulderside.usecase.user.dto.response;

import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import lombok.Builder;

@Builder
public record CreateUserCommand(
	String nickname,
	UserRole userRole
) {
}
