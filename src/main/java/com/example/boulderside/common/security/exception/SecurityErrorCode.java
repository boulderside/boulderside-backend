package com.example.boulderside.common.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SecurityErrorCode {
	// Authentication (S001 ~ S049)
	AUTHENTICATION_FAILED("S001", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),

	ACCESS_TOKEN_EXPIRED("S002", "Access Token이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
	ACCESS_TOKEN_MISSING("S003", "Access Token이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
	ACCESS_TOKEN_INVALID("S004", "유효하지 않은 Access Token입니다.", HttpStatus.UNAUTHORIZED),

	REFRESH_TOKEN_EXPIRED("S005", "Refresh Token이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
	REFRESH_TOKEN_INVALID("S006", "유효하지 않은 Refresh Token입니다.", HttpStatus.UNAUTHORIZED),
	REFRESH_TOKEN_MISSING("S007", "Refresh Token이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
	REFRESH_TOKEN_MISMATCH("S008", "요청한 Refresh Token이 서버의 값과 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

	// Authorization (S050 ~ S099)
	ACCESS_DENIED("S050", "요청한 리소스에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
	ROLE_NOT_ALLOWED("S051", "해당 역할(Role)로 접근이 불가능합니다.", HttpStatus.FORBIDDEN);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	SecurityErrorCode(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
