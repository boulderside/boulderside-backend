package com.line7studio.boulderside.controller.report;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.report.request.CreateReportRequest;
import com.line7studio.boulderside.controller.report.response.MyReportListResponse;
import com.line7studio.boulderside.controller.report.response.ReportResponse;
import com.line7studio.boulderside.usecase.report.ReportUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportUseCase reportUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(
        @Valid @RequestBody CreateReportRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReportResponse response = reportUseCase.createReport(request, userDetails.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyReportListResponse>> getMyReports(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        MyReportListResponse response = reportUseCase.getMyReports(userDetails.userId(), page, size);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
