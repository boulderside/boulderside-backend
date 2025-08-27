package com.line7studio.boulderside.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// Domain (D001~D099)
	USER_NOT_FOUND("D001", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	BOULDER_NOT_FOUND("D002", "해당 바위를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	USER_INACTIVE("D003", "비활성 상태의 사용자입니다.", HttpStatus.BAD_REQUEST),
	ALREADY_LIKED("D004", "이미 좋아요를 누른 항목입니다.", HttpStatus.CONFLICT),
	REGION_NOT_FOUND("D005", "해당 지역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_NOT_FOUND("D006", "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("D007", "해당 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_PERMISSION("D008", "해당 작업에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),

	// Validation (V001~V099)
	VALIDATION_FAILED("V001", "입력 값 검증에 실패했습니다.", HttpStatus.BAD_REQUEST),
	CONSTRAINT_VIOLATION("V002", "비즈니스 제약조건을 위반했습니다.", HttpStatus.BAD_REQUEST),
	MISSING_REQUIRED_FIELD("V003", "필수 입력 값이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    NOT_SUPPORT_SORT_TYPE("V004", "지원하지 않는 정렬 타입입니다.", HttpStatus.BAD_REQUEST),

	// Persistence (P001~P099)
	DB_CONSTRAINT_ERROR("P001", "데이터베이스 제약조건 위반입니다.", HttpStatus.CONFLICT),
	ENTITY_LOCK_ERROR("P002", "낙관적 락(Optimistic Lock) 오류가 발생했습니다.", HttpStatus.CONFLICT),
	DATA_ACCESS_ERROR("P003", "데이터 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	// Application (A001~A099)
	EXTERNAL_API_ERROR("A001", "외부 API 호출에 실패했습니다.", HttpStatus.SERVICE_UNAVAILABLE),
	EMAIL_SEND_FAILED("A002", "이메일 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	PHONE_VERIFICATION_FAILED("A003", "휴대폰 번호 인증에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	AES_ENCRYPTION_ERROR("A004", "데이터 암호화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	AES_DECRYPTION_ERROR("A005", "데이터 복호화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	ACCOUNT_ALREADY_EXISTS("A006", "이미 존재하는 계정입니다.", HttpStatus.CONFLICT),
	PHONE_SEND_FAILED("A007", "휴대폰 인증번호 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	REDIS_STORE_FAILED("A008", "인증 정보를 저장하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	S3_UPLOAD_FAILED("A009", "S3 이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	S3_DELETE_FAILED("A010", "S3 이미지 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	S3_INVALID_FILE_TYPE("A011", "지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST),
	UNKNOWN_ERROR("A999", "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	// External (E001~E099)
	LATITUDE_OUT_OF_RANGE("E001", "위도의 범위를 벗어났습니다.", HttpStatus.BAD_REQUEST),
	LONGITUDE_OUT_OF_RANGE("E002", "경도의 범위를 벗어났습니다.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	ErrorCode(String code, String errorMessage, HttpStatus httpStatus) {
		this.code = code;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
}
