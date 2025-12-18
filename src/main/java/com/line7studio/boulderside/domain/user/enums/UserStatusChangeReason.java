package com.line7studio.boulderside.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusChangeReason {
    USER_REQUEST("사용자 요청"),
    ADMIN_ACTION("관리자 조치"),
    SYSTEM_AUTO("시스템 자동 처리"),
    POLICY_VIOLATION("정책 위반"),
    DORMANT_ACCOUNT("휴면 계정 전환");

    private final String description;
}
