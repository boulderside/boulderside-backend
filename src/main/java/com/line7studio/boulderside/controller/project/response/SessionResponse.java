package com.line7studio.boulderside.controller.project.response;

import com.line7studio.boulderside.domain.project.Session;

import java.time.LocalDate;

public record SessionResponse(
    LocalDate sessionDate,
    Integer sessionCount
) {
    public static SessionResponse from(Session history) {
        return new SessionResponse(
            history.getSessiondDate(),
            history.getSessionCount()
        );
    }
}
