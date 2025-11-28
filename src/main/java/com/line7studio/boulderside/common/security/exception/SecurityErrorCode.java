package com.line7studio.boulderside.common.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SecurityErrorCode {
	UNKNOWN_AUTHENTICATION_ERROR("S000", "인증 처리 중 알 수 없는 오류가 발생했습니다.", HttpStatus.UNAUTHORIZED),

	// Authentication (S001 ~ S049)
	INVALID_USERNAME("S001", "아이디가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
	INVALID_PASSWORD("S002", "비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
	AUTHENTICATION_PAYLOAD_INVALID("S003", "인증 처리 중 알 수 없는 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),

	ACCESS_TOKEN_EXPIRED("S010", "Access Token이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
	ACCESS_TOKEN_MISSING("S011", "Access Token이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
	ACCESS_TOKEN_INVALID("S012", "유효하지 않은 Access Token입니다.", HttpStatus.UNAUTHORIZED),

	REFRESH_TOKEN_EXPIRED("S020", "Refresh Token이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
	REFRESH_TOKEN_INVALID("S021", "유효하지 않은 Refresh Token입니다.", HttpStatus.UNAUTHORIZED),
	REFRESH_TOKEN_MISSING("S022", "Refresh Token이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
	REFRESH_TOKEN_MISMATCH("S023", "요청한 Refresh Token이 서버의 값과 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

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