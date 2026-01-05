package com.line7studio.boulderside.controller.report.request;

import com.line7studio.boulderside.domain.report.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateReportStatusRequest(
    @NotNull(message = "신고 상태는 필수입니다.")
    ReportStatus status
) {
}
