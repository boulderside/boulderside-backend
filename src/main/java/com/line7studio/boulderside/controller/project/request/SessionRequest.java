package com.line7studio.boulderside.controller.project.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SessionRequest(
    @NotNull(message = "세션 날짜는 필수입니다.")
    LocalDate sessionDate,

    @NotNull(message = "세션 횟수는 필수입니다.")
    @Min(value = 0, message = "세션 횟수는 0 이상이어야 합니다.")
    Integer sessionCount
) {}
