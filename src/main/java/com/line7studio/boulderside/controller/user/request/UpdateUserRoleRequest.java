package com.line7studio.boulderside.controller.user.request;

import com.line7studio.boulderside.domain.user.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
    @NotNull(message = "사용자 권한은 필수입니다.")
    UserRole userRole
) {}