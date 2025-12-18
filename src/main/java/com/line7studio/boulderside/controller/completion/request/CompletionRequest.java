package com.line7studio.boulderside.controller.completion.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CompletionRequest(
	@NotNull Long routeId,
	@NotNull LocalDate completedDate,
	@Size(max = 500) String memo
) {}
