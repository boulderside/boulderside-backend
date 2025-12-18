package com.line7studio.boulderside.controller.project.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.route.Route;

import java.time.LocalDate;
import java.util.List;

public record ProjectRecordSummaryResponse(
    Level highestCompletedLevel,
    long completedRouteCount,
    long ongoingProjectCount,
    List<CompletedRouteResponse> completedRoutes
) {
    public record CompletedRouteResponse(
        Long routeId,
        LocalDate completedDate,
        Level routeLevel
    ) {
        public static CompletedRouteResponse of(Route route, LocalDate completedDate) {
            return new CompletedRouteResponse(
                route.getId(),
                completedDate,
                route.getRouteLevel()
            );
        }
    }
}
