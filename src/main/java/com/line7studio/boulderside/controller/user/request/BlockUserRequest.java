package com.line7studio.boulderside.controller.user.request;

import jakarta.validation.constraints.NotNull;

public record BlockUserRequest(
    @NotNull
    Long targetUserId
) {
}
