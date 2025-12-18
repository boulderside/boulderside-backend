package com.line7studio.boulderside.domain.report.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.report.Report;
import com.line7studio.boulderside.domain.report.enums.ReportStatus;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import com.line7studio.boulderside.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public Page<Report> getReportsByReporter(Long reporterId, Pageable pageable) {
        return reportRepository.findByReporterId(reporterId, pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsByReporterAndTarget(Long reporterId, ReportTargetType targetType, Long targetId) {
        return reportRepository.existsByReporterIdAndTargetTypeAndTargetId(reporterId, targetType, targetId);
    }

    @Transactional(readOnly = true)
    public Report getReportById(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Report> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    @Transactional
    public Report updateStatus(Long reportId, ReportStatus status) {
        Report report = getReportById(reportId);
        report.updateStatus(status);
        return report;
    }
}
