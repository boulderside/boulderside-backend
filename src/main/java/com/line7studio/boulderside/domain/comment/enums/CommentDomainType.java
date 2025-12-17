package com.line7studio.boulderside.domain.comment.enums;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;

import java.util.Arrays;


public enum CommentDomainType {
    BOARD_POST("board-posts"),
    MATE_POST("mate-posts"),
    ROUTE("routes");

    private final String pathName;

    CommentDomainType(String pathName) {
        this.pathName = pathName;
    }

    public static CommentDomainType fromPath(String path) {
        return Arrays.stream(values())
                .filter(type -> type.pathName.equals(path))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException(ErrorCode.VALIDATION_FAILED));
    }
}
