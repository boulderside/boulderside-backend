package com.example.boulderside.common.exception;

import com.example.boulderside.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
	private final ErrorCode errorCode;

	public ValidationException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}

}
