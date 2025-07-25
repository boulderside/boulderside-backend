package com.example.boulderside.common.exception;

import lombok.Getter;

@Getter
public class DatabaseException extends RuntimeException {
	private final ErrorCode errorCode;

	public DatabaseException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
