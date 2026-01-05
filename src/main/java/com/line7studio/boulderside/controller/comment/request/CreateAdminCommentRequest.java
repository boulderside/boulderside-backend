package com.line7studio.boulderside.controller.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAdminCommentRequest(
    @NotBlank(message = "도메인 타입은 필수입니다.")
    @Size(min = 1, max = 50, message = "도메인 타입은 1자 이상 50자 이하여야 합니다.")
    String domainType,

    @NotNull(message = "도메인 ID는 필수입니다.")
    Long domainId,

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(min = 1, max = 1000, message = "댓글은 1자 이상 1000자 이하여야 합니다.")
    String content,

    Long userId
) {}