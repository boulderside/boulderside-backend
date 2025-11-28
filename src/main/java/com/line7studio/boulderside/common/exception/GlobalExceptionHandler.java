package com.line7studio.boulderside.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.line7studio.boulderside.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorResponse body = new ErrorResponse(ErrorCode.UNKNOWN_ERROR.getCode(),
			ErrorCode.UNKNOWN_ERROR.getErrorMessage());
		log.error("UnhandledException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.UNKNOWN_ERROR.getHttpStatus())
			.body(body);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleApplicationException(BusinessException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getErrorMessage());
		log.error("ApplicationException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ErrorResponse> handleDatabaseException(DatabaseException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getErrorMessage());
		log.error("DatabaseException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getErrorMessage());
		log.error("DomainException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(ExternalApiException.class)
	public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getErrorMessage());
		log.error("ExternalApiException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.findFirst()
			.orElse(ErrorCode.VALIDATION_FAILED.getErrorMessage());

		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
		log.error("MethodArgumentNotValidException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.findFirst()
			.orElse(ErrorCode.VALIDATION_FAILED.getErrorMessage());

		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
		log.error("BindException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException ex) {

		assert ex.getRequiredType() != null;
		String errorMessage = String.format("파라미터 '%s'의 값 '%s'이(가) 잘못된 형식입니다. 올바른 형식: %s",
			ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
		log.error("MethodArgumentTypeMismatchException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException ex) {
		String errorMessage = String.format("필수 파라미터 '%s'이(가) 누락되었습니다.", ex.getParameterName());

		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.MISSING_REQUIRED_FIELD.getCode(), errorMessage);
		log.error("MissingServletRequestParameterException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.MISSING_REQUIRED_FIELD.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		String errorMessage = "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요.";

		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
		log.error("HttpMessageNotReadableException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException ex) {
		String errorMessage = String.format("지원하지 않는 HTTP 메서드입니다: %s", ex.getMethod());

		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
		log.error("HttpRequestMethodNotSupportedException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
		HttpMediaTypeNotSupportedException ex) {
		String errorMessage = String.format("지원하지 않는 Content-Type입니다: %s", ex.getContentType());

		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
		log.error("HttpMediaTypeNotSupportedException caught in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getErrorMessage());
		log.error("ValidationException in GlobalExceptionHandler", ex);
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(errorResponse);
	}
}
