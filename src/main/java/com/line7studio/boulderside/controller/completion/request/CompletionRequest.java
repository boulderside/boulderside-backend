package com.line7studio.boulderside.controller.completion.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CompletionRequest(
	@NotNull(message = "루트 ID는 필수입니다.")
	Long routeId,

	@NotNull(message = "완등 날짜는 필수입니다.")
	LocalDate completedDate,

	@Size(max = 500, message = "메모는 500자 이하여야 합니다.")
	String memo
) {}
