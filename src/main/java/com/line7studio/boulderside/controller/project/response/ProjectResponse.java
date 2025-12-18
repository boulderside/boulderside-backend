package com.line7studio.boulderside.controller.project.response;

import com.line7studio.boulderside.domain.project.Project;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public record ProjectResponse(
    Long projectId,
    Long routeId,
    Long userId,
    Boolean completed,
    Boolean registered,
    String memo,
    List<SessionResponse> sessions,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ProjectResponse from(Project project) {
        List<SessionResponse> histories = project.getSessions() == null
            ? Collections.emptyList()
            : project.getSessions().stream()
                .map(SessionResponse::from)
                .sorted(Comparator.comparing(SessionResponse::sessionDate).reversed())
                .toList();

        return new ProjectResponse(
            project.getId(),
            project.getRouteId(),
            project.getUserId(),
            project.getCompleted(),
            true,
            project.getMemo(),
            histories,
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
}
