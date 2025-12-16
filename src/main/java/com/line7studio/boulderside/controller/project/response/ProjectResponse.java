package com.line7studio.boulderside.controller.project.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.project.entity.Project;
import com.line7studio.boulderside.domain.feature.route.entity.Route;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
public record ProjectResponse(Long projectId, Long routeId, Long userId, Boolean completed, String memo,
                              RouteInfo routeInfo,
                              List<ProjectAttemptHistoryResponse> attemptHistories, LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

    @Builder
    public record RouteInfo(
            String name,
            Level routeLevel,
            Long climberCount,
            Long likeCount,
            Long viewCount,
            Long commentCount
    ) {
        public static RouteInfo from(Route route) {
            return RouteInfo.builder()
                    .name(route.getName())
                    .routeLevel(route.getRouteLevel())
                    .climberCount(route.getClimberCount())
                    .likeCount(route.getLikeCount())
                    .viewCount(route.getViewCount())
                    .commentCount(route.getCommentCount())
                    .build();
        }
    }

    public static ProjectResponse from(Project project, Route route) {
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
                .routeInfo(RouteInfo.from(route))
                .attemptHistories(histories)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
