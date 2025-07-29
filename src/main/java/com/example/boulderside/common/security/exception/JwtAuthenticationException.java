package com.example.boulderside.common.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
	private final SecurityErrorCode errorCode;

	public JwtAuthenticationException(SecurityErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
