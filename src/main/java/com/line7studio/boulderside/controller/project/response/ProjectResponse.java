package com.line7studio.boulderside.controller.project.response;

import com.line7studio.boulderside.domain.feature.project.entity.Project;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
public record ProjectResponse(Long projectId, Long routeId, Long userId, Boolean completed, String memo,
                              List<ProjectAttemptHistoryResponse> attemptHistories, LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
    public static ProjectResponse from(Project project) {
        List<ProjectAttemptHistoryResponse> histories = project.getAttemptHistories() == null
                ? Collections.emptyList()
                : project.getAttemptHistories().stream()
                .map(ProjectAttemptHistoryResponse::from)
                .sorted(java.util.Comparator.comparing(ProjectAttemptHistoryResponse::getAttemptedDate).reversed())
                .toList();

        return ProjectResponse.builder()
                .projectId(project.getId())
                .routeId(project.getRouteId())
                .userId(project.getUserId())
                .completed(project.getCompleted())
                .memo(project.getMemo())
                .attemptHistories(histories)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
