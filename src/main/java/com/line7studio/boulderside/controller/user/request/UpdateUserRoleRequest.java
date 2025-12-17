package com.line7studio.boulderside.controller.user.request;

import com.line7studio.boulderside.domain.user.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
    @NotNull UserRole userRole
) {}