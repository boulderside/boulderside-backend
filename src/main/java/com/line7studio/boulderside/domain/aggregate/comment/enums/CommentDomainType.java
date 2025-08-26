package com.line7studio.boulderside.domain.aggregate.comment.enums;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ValidationException;

import java.util.Arrays;


public enum CommentDomainType {
    POST("posts"),
    ROUTE("routes");

    private final String pathName;

    CommentDomainType(String pathName) {
        this.pathName = pathName;
    }

    public static CommentDomainType fromPath(String path) {
        return Arrays.stream(values())
                .filter(type -> type.pathName.equals(path))
                .findFirst()
                .orElseThrow(() -> new ValidationException(ErrorCode.VALIDATION_FAILED));
    }
}
