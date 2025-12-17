package com.line7studio.boulderside.common.exception;

import lombok.Getter;

@Getter
public class InvalidValueException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public InvalidValueException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }
}
