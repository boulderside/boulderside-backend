package com.line7studio.boulderside.domain.report;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.report.enums.ReportCategory;
import com.line7studio.boulderside.domain.report.enums.ReportStatus;
import com.line7studio.boulderside.domain.report.enums.ReportTargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private ReportTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ReportCategory category;

    @Column(name = "reason", length = 2000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReportStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private Report(Long id, Long reporterId, ReportTargetType targetType, Long targetId, ReportCategory category, String reason,
                   ReportStatus status) {
        this.id = id;
        this.reporterId = reporterId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.category = category;
        this.reason = reason;
        this.status = status;
    }

    public static Report create(Long reporterId, ReportTargetType targetType, Long targetId, ReportCategory category, String reason) {
        validateReporter(reporterId);
        validateReason(reason);
        if (targetType == null) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "신고 대상 유형은 필수입니다.");
        }
        if (category == null) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "신고 카테고리는 필수입니다.");
        }

        return Report.builder()
            .reporterId(reporterId)
            .targetType(targetType)
            .targetId(targetId)
            .category(category)
            .reason(reason)
            .status(ReportStatus.PENDING)
            .build();
    }

    public void updateStatus(ReportStatus nextStatus) {
        if (nextStatus == null) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "상태 변경 값이 필요합니다.");
        }
        this.status = nextStatus;
    }

    private static void validateReporter(Long reporterId) {
        if (reporterId == null) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "신고자는 필수입니다.");
        }
    }

    private static void validateReason(String reason) {
        if (reason.length() > 2000) {
            throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "신고 사유는 2000자를 초과할 수 없습니다.");
        }
    }
}
