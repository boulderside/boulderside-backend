package com.line7studio.boulderside.controller.report.response;

import com.line7studio.boulderside.domain.report.Report;
import com.line7studio.boulderside.domain.report.enums.ReportCategory;
import com.line7studio.boulderside.domain.report.enums.ReportStatus;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import java.time.LocalDateTime;

public record ReportResponse(
    Long reportId,
    ReportTargetType targetType,
    Long targetId,
    ReportCategory category,
    String reason,
    ReportStatus status,
    String displayStatus,
    LocalDateTime createdAt
) {

    public static ReportResponse from(Report report) {
        return new ReportResponse(
            report.getId(),
            report.getTargetType(),
            report.getTargetId(),
            report.getCategory(),
            report.getReason(),
            report.getStatus(),
            report.getStatus().userDisplayStatus(),
            report.getCreatedAt()
        );
    }
}
