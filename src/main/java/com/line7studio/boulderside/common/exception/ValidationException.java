package com.line7studio.boulderside.common.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
	private final ErrorCode errorCode;

	public ValidationException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}

}
