package com.line7studio.boulderside.common.exception;

import lombok.Getter;

@Getter
public class DatabaseException extends RuntimeException {
	private final ErrorCode errorCode;

	public DatabaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
