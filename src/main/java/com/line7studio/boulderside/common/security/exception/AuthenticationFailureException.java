package com.line7studio.boulderside.common.security.exception;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;

@Getter
public class AuthenticationFailureException extends AuthenticationException {
	private final SecurityErrorCode errorCode;

	public AuthenticationFailureException(SecurityErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
