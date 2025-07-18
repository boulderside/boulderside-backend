package com.example.boulderside.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// Domain (D001~D099)
	USER_NOT_FOUND("D001", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	USER_INACTIVE("D003", "비활성 상태의 사용자입니다.", HttpStatus.BAD_REQUEST),

	// Validation (V001~V099)
	VALIDATION_FAILED("V001", "입력 값 검증에 실패했습니다.", HttpStatus.BAD_REQUEST),
	CONSTRAINT_VIOLATION("V002", "비즈니스 제약조건을 위반했습니다.", HttpStatus.BAD_REQUEST),

	// Persistence (P001~P099)
	DB_CONSTRAINT_ERROR("P001", "데이터베이스 제약조건 위반입니다.", HttpStatus.CONFLICT),
	ENTITY_LOCK_ERROR("P002", "낙관적 락(Optimistic Lock) 오류가 발생했습니다.", HttpStatus.CONFLICT),
	DATA_ACCESS_ERROR("P003", "데이터 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	// Application (A001~A099)
	EXTERNAL_API_ERROR("A001", "외부 API 호출에 실패했습니다.", HttpStatus.SERVICE_UNAVAILABLE),
	EMAIL_SEND_FAILED("A002", "이메일 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	PHONE_VERIFICATION_FAILED("A003", "휴대폰 번호 인증에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	UNKNOWN_ERROR("A999", "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	ErrorCode(String code, String errorMessage, HttpStatus httpStatus) {
		this.code = code;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
}
