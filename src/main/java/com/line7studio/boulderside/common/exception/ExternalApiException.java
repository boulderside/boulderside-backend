package com.line7studio.boulderside.common.exception;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
	private final ErrorCode errorCode;

	public ExternalApiException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
