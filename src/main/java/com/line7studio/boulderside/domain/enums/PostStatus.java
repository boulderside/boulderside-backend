package com.line7studio.boulderside.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
    ACTIVE("활성"),
    BLOCKED("차단됨"),
    DELETED("삭제됨");

    private final String description;
}