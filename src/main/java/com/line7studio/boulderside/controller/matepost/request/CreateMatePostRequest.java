package com.line7studio.boulderside.controller.matepost.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateMatePostRequest(
    @NotBlank(message = "제목은 필수입니다") String title,
    @NotBlank(message = "내용은 필수입니다") String content,
    @NotNull(message = "동행 날짜는 필수입니다") LocalDate meetingDate
) {}