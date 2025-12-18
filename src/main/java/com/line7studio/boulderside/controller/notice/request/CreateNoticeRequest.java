package com.line7studio.boulderside.controller.notice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNoticeRequest(
    @NotBlank
    @Size(max = 150)
    String title,
    @NotBlank
    String content,
    boolean pinned
) {
}
