package com.line7studio.boulderside.common.exception;

import lombok.Getter;

@Getter
public class ExternalException extends RuntimeException {
    private final ErrorCode errorCode;

    public ExternalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ExternalException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }
}
