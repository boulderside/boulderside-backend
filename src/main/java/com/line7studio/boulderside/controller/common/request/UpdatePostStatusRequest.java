package com.line7studio.boulderside.controller.common.request;

import com.line7studio.boulderside.domain.enums.PostStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePostStatusRequest(
    @NotNull(message = "게시물 상태는 필수입니다.")
    PostStatus status
) {
}