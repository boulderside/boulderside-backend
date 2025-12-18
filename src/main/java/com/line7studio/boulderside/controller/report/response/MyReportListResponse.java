package com.line7studio.boulderside.controller.report.response;

import java.util.List;

public record MyReportListResponse(
    List<MyReportResponse> reports,
    int page,
    int size,
    long totalElements,
    boolean hasNext
) {
    public static MyReportListResponse of(List<MyReportResponse> reports, int page, int size, long totalElements, boolean hasNext) {
        return new MyReportListResponse(reports, page, size, totalElements, hasNext);
    }
}
