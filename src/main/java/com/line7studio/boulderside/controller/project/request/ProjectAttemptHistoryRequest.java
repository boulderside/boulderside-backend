package com.line7studio.boulderside.controller.project.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectAttemptHistoryRequest {
	@NotNull
	private LocalDate attemptedDate;

	@NotNull
	@Min(0)
	private Integer attemptCount;
}
