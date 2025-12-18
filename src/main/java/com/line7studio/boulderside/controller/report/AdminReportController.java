package com.line7studio.boulderside.controller.report;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.report.request.UpdateReportStatusRequest;
import com.line7studio.boulderside.controller.report.response.AdminReportListResponse;
import com.line7studio.boulderside.controller.report.response.AdminReportResponse;
import com.line7studio.boulderside.usecase.report.ReportUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportUseCase reportUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminReportListResponse>> getReports(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        AdminReportListResponse response = reportUseCase.getAllReports(page, size);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @PutMapping("/{reportId}/status")
    public ResponseEntity<ApiResponse<AdminReportResponse>> updateReportStatus(
        @PathVariable Long reportId,
        @Valid @RequestBody UpdateReportStatusRequest request
    ) {
        AdminReportResponse response = reportUseCase.updateReportStatus(reportId, request.status());
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
