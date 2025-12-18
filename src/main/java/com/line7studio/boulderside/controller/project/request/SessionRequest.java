package com.line7studio.boulderside.controller.project.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SessionRequest(
    @NotNull LocalDate sessionDate,
    @NotNull @Min(0) Integer sessionCount
) {}
