package com.line7studio.boulderside.controller.user.request;

import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRoleRequest {
	@NotNull
	private UserRole userRole;
}
