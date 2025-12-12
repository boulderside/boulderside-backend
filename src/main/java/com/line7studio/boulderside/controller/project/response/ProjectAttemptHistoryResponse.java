package com.line7studio.boulderside.controller.project.response;

import java.time.LocalDate;

import com.line7studio.boulderside.domain.feature.project.entity.ProjectAttemptHistory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectAttemptHistoryResponse {
	private final LocalDate attemptedDate;
	private final Integer attemptCount;

	public static ProjectAttemptHistoryResponse from(ProjectAttemptHistory history) {
		return ProjectAttemptHistoryResponse.builder()
			.attemptedDate(history.getAttemptedDate())
			.attemptCount(history.getAttemptCount())
			.build();
	}
}
