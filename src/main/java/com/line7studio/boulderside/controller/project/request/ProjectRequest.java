package com.line7studio.boulderside.controller.project.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
    Long routeId,
    @NotNull Boolean completed,
    @Size(max = 500) String memo,
    @Valid List<ProjectAttemptHistoryRequest> attemptHistories
) {}