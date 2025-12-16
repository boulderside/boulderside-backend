package com.line7studio.boulderside.controller.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAdminCommentRequest(
    @NotBlank String domainType,
    @NotNull Long domainId,
    @NotBlank String content,
    Long userId
) {}