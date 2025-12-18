package com.line7studio.boulderside.controller.report.request;

import com.line7studio.boulderside.domain.report.enums.ReportCategory;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReportRequest(
    @NotNull ReportTargetType targetType,
    @NotNull Long targetId,
    @NotNull ReportCategory category,
    @Size(max = 2000) String reason
) {
}
