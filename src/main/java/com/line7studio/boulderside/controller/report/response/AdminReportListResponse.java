package com.line7studio.boulderside.controller.report.response;

import java.util.List;

public record AdminReportListResponse(
    List<AdminReportResponse> reports,
    int page,
    int size,
    long totalElements,
    boolean hasNext
) {

    public static AdminReportListResponse of(List<AdminReportResponse> reports, int page, int size, long totalElements, boolean hasNext) {
        return new AdminReportListResponse(reports, page, size, totalElements, hasNext);
    }
}
