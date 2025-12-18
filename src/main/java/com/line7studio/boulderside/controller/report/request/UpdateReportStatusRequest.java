package com.line7studio.boulderside.controller.report.request;

import com.line7studio.boulderside.domain.report.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateReportStatusRequest(
    @NotNull
    ReportStatus status
) {
}
