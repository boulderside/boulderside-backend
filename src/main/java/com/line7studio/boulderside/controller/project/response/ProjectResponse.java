package com.line7studio.boulderside.controller.project.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.project.Project;
import com.line7studio.boulderside.domain.route.Route;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public record ProjectResponse(
    Long projectId,
    Long routeId,
    Long userId,
    Boolean completed,
    String memo,
    RouteInfo routeInfo,
    List<AttemptResponse> attempts,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record RouteInfo(
        String name,
        Level routeLevel,
        Long climberCount,
        Long likeCount,
        Long viewCount,
        Long commentCount
    ) {
        public static RouteInfo from(Route route) {
            return new RouteInfo(
                route.getName(),
                route.getRouteLevel(),
                route.getClimberCount(),
                route.getLikeCount(),
                route.getViewCount(),
                route.getCommentCount()
            );
        }
    }

    public static ProjectResponse from(Project project, Route route) {
        List<AttemptResponse> histories = project.getAttempts() == null
            ? Collections.emptyList()
            : project.getAttempts().stream()
                .map(AttemptResponse::from)
                .sorted(Comparator.comparing(AttemptResponse::attemptedDate).reversed())
                .toList();

        return new ProjectResponse(
            project.getId(),
            project.getRouteId(),
            project.getUserId(),
            project.getCompleted(),
            project.getMemo(),
            RouteInfo.from(route),
            histories,
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
}
