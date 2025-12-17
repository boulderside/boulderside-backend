package com.line7studio.boulderside.controller.project.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProjectAttemptHistoryRequest(
    @NotNull LocalDate attemptedDate,
    @NotNull @Min(0) Integer attemptCount
) {}