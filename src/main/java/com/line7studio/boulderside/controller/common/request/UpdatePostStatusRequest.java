package com.line7studio.boulderside.controller.common.request;

import com.line7studio.boulderside.domain.enums.PostStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePostStatusRequest(
    @NotNull
    PostStatus status
) {
}