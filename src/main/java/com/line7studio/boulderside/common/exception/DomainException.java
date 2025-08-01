package com.line7studio.boulderside.common.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
	private final ErrorCode errorCode;

	public DomainException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
