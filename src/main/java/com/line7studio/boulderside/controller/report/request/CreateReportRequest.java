package com.line7studio.boulderside.controller.report.request;

import com.line7studio.boulderside.domain.report.enums.ReportCategory;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReportRequest(
    @NotNull(message = "신고 대상 타입은 필수입니다.")
    ReportTargetType targetType,

    @NotNull(message = "신고 대상 ID는 필수입니다.")
    Long targetId,

    @NotNull(message = "신고 카테고리는 필수입니다.")
    ReportCategory category,

    @Size(max = 2000, message = "신고 사유는 2000자 이하여야 합니다.")
    String reason
) {
}
