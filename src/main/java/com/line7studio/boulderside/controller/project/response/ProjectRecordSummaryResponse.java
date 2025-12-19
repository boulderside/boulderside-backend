package com.line7studio.boulderside.controller.project.response;

import com.line7studio.boulderside.common.enums.Level;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ProjectRecordSummaryResponse(
    Level highestCompletedLevel,
    long completedRouteCount,
    long ongoingProjectCount,
    List<CompletedRouteCountResponse> completedRoutes,
    Map<Level, List<Long>> completionIdsByLevel
) {
    public record CompletedRouteCountResponse(
        LocalDate completedDate,
        long cumulativeCount
    ) {
        public static CompletedRouteCountResponse of(LocalDate completedDate, long cumulativeCount) {
            return new CompletedRouteCountResponse(completedDate, cumulativeCount);
        }
    }
}
