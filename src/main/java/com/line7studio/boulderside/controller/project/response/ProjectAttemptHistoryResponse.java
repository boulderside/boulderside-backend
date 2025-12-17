package com.line7studio.boulderside.controller.project.response;

import java.time.LocalDate;

import com.line7studio.boulderside.domain.feature.project.ProjectAttemptHistory;

public record ProjectAttemptHistoryResponse(
    LocalDate attemptedDate,
    Integer attemptCount
) {
    public static ProjectAttemptHistoryResponse from(ProjectAttemptHistory history) {
        return new ProjectAttemptHistoryResponse(
            history.getAttemptedDate(),
            history.getAttemptCount()
        );
    }
}