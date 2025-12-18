package com.line7studio.boulderside.controller.report.response;

import com.line7studio.boulderside.domain.report.Report;
import com.line7studio.boulderside.domain.report.enums.ReportCategory;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import java.time.LocalDateTime;

public record MyReportResponse(
    Long reportId,
    ReportTargetType targetType,
    Long targetId,
    ReportCategory category,
    String reason,
    String displayStatus,
    LocalDateTime createdAt
) {

    public static MyReportResponse from(Report report) {
        return new MyReportResponse(
            report.getId(),
            report.getTargetType(),
            report.getTargetId(),
            report.getCategory(),
            report.getReason(),
            report.getStatus().userDisplayStatus(),
            report.getCreatedAt()
        );
    }
}
