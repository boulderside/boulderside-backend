package com.line7studio.boulderside.controller.report.response;

import com.line7studio.boulderside.domain.report.Report;
import com.line7studio.boulderside.domain.report.enums.ReportCategory;
import com.line7studio.boulderside.domain.report.enums.ReportStatus;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import java.time.LocalDateTime;

public record AdminReportResponse(
    Long reportId,
    Long reporterId,
    ReportTargetType targetType,
    Long targetId,
    ReportCategory category,
    String reason,
    ReportStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static AdminReportResponse from(Report report) {
        return new AdminReportResponse(
            report.getId(),
            report.getReporterId(),
            report.getTargetType(),
            report.getTargetId(),
            report.getCategory(),
            report.getReason(),
            report.getStatus(),
            report.getCreatedAt(),
            report.getUpdatedAt()
        );
    }
}
