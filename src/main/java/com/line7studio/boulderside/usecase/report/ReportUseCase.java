package com.line7studio.boulderside.usecase.report;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.controller.report.request.CreateReportRequest;
import com.line7studio.boulderside.controller.report.response.AdminReportListResponse;
import com.line7studio.boulderside.controller.report.response.AdminReportResponse;
import com.line7studio.boulderside.controller.report.response.MyReportListResponse;
import com.line7studio.boulderside.controller.report.response.MyReportResponse;
import com.line7studio.boulderside.controller.report.response.ReportResponse;
import com.line7studio.boulderside.domain.report.Report;
import com.line7studio.boulderside.domain.report.enums.ReportStatus;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import com.line7studio.boulderside.domain.report.service.ReportService;
import com.line7studio.boulderside.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportUseCase {

    private final ReportService reportService;
    private final UserService userService;

    @Transactional
    public ReportResponse createReport(CreateReportRequest request, Long reporterId) {
        userService.getUserById(reporterId);
        ReportTargetType targetType = request.targetType();
        if (reportService.existsByReporterAndTarget(reporterId, targetType, request.targetId())) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VIOLATION, "이미 해당 대상을 신고했습니다.");
        }
        Report report = Report.create(reporterId, targetType, request.targetId(), request.category(), request.reason());
        Report saved = reportService.createReport(report);
        return ReportResponse.from(saved);
    }

    public MyReportListResponse getMyReports(Long reporterId, int page, int size) {
        userService.getUserById(reporterId);
        Pageable pageable = buildPageable(page, size);
        Page<Report> reportPage = reportService.getReportsByReporter(reporterId, pageable);
        List<MyReportResponse> responses = reportPage.getContent().stream()
            .map(MyReportResponse::from)
            .toList();
        return MyReportListResponse.of(responses, reportPage.getNumber(), reportPage.getSize(), reportPage.getTotalElements(), reportPage.hasNext());
    }

    public AdminReportListResponse getAllReports(int page, int size) {
        Pageable pageable = buildPageable(page, size);
        Page<Report> reportPage = reportService.getAllReports(pageable);
        List<AdminReportResponse> responses = reportPage.getContent().stream()
            .map(AdminReportResponse::from)
            .toList();
        return AdminReportListResponse.of(responses, reportPage.getNumber(), reportPage.getSize(), reportPage.getTotalElements(), reportPage.hasNext());
    }

    @Transactional
    public AdminReportResponse updateReportStatus(Long reportId, ReportStatus status) {
        Report report = reportService.updateStatus(reportId, status);
        return AdminReportResponse.from(report);
    }

    private Pageable buildPageable(int page, int size) {
        int normalizedPage = Math.max(page, 0);
        int normalizedSize = size <= 0 ? 20 : Math.min(size, 50);
        return PageRequest.of(normalizedPage, normalizedSize, Sort.by(Sort.Direction.DESC, "id"));
    }
}
