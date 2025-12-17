package com.line7studio.boulderside.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    PENDING("가입 대기"),
    ACTIVE("활성"),
    INACTIVE("비활성"),
    BANNED("정지");

    private final String description;
}
