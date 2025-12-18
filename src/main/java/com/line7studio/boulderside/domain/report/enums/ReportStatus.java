package com.line7studio.boulderside.domain.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportStatus {
    PENDING("접수"),
    UNDER_REVIEW("검토중"),
    RESOLVED("처리완료"),
    REJECTED("무효/반려");

    private final String description;

    public String userDisplayStatus() {
        return switch (this) {
            case PENDING, UNDER_REVIEW -> "검토중";
            case RESOLVED, REJECTED -> "처리완료";
        };
    }
}
