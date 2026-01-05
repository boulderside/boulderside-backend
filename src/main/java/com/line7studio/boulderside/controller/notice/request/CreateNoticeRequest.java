package com.line7studio.boulderside.controller.notice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNoticeRequest(
    @NotBlank(message = "공지 제목은 필수입니다.")
    @Size(min = 1, max = 150, message = "공지 제목은 1자 이상 150자 이하여야 합니다.")
    String title,

    @NotBlank(message = "공지 내용은 필수입니다.")
    @Size(min = 1, max = 10000, message = "공지 내용은 1자 이상 10000자 이하여야 합니다.")
    String content,

    boolean pinned
) {
}
