package com.line7studio.boulderside.controller.project.response;

import java.time.LocalDate;

import com.line7studio.boulderside.domain.project.Attempt;

public record AttemptResponse(
    LocalDate attemptedDate,
    Integer attemptCount
) {
    public static AttemptResponse from(Attempt history) {
        return new AttemptResponse(
            history.getAttemptedDate(),
            history.getAttemptCount()
        );
    }
}
