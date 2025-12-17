package com.line7studio.boulderside.controller.boardpost.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateBoardPostRequest(
    @NotBlank(message = "제목은 필수입니다") String title,
    @NotBlank(message = "내용은 필수입니다") String content
) {}