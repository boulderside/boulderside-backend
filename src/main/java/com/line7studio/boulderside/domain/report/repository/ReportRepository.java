package com.line7studio.boulderside.domain.report.repository;

import com.line7studio.boulderside.domain.report.Report;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByReporterId(Long reporterId, Pageable pageable);

    boolean existsByReporterIdAndTargetTypeAndTargetId(Long reporterId, ReportTargetType targetType, Long targetId);
}
