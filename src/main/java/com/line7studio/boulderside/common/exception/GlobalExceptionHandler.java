package com.line7studio.boulderside.common.exception;

import com.line7studio.boulderside.common.response.ErrorDetail;
import com.line7studio.boulderside.common.response.ErrorResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("UnhandledException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR.getMessage());
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleApplicationException(BusinessException ex) {
		log.error("ApplicationException caught in GlobalExceptionHandler", ex);
		return respond(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ErrorResponse> handleDatabaseException(DatabaseException ex) {
		log.error("DatabaseException caught in GlobalExceptionHandler", ex);
		return respond(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
		log.error("DomainException caught in GlobalExceptionHandler", ex);
		return respond(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(ExternalApiException.class)
	public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException ex) {
		log.error("ExternalApiException caught in GlobalExceptionHandler", ex);
		return respond(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ErrorDetail> errorDetails = toErrorDetails(ex.getBindingResult());
		log.error("MethodArgumentNotValidException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.VALIDATION_FAILED, ErrorCode.VALIDATION_FAILED.getMessage(), errorDetails);
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
		List<ErrorDetail> errorDetails = toErrorDetails(ex.getBindingResult());
		log.error("BindException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.VALIDATION_FAILED, ErrorCode.VALIDATION_FAILED.getMessage(), errorDetails);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException ex) {
		String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
		String reason = String.format("파라미터 '%s'의 값 '%s'이(가) 잘못된 형식입니다. 올바른 형식: %s",
			ex.getName(), Objects.toString(ex.getValue(), "null"), requiredType);
		List<ErrorDetail> errorDetails = List.of(ErrorDetail.of(ex.getName(), Objects.toString(ex.getValue(), null), reason));
		log.error("MethodArgumentTypeMismatchException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.VALIDATION_FAILED, ErrorCode.VALIDATION_FAILED.getMessage(), errorDetails);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException ex) {
		String reason = String.format("필수 파라미터 '%s'이(가) 누락되었습니다.", ex.getParameterName());
		List<ErrorDetail> errorDetails = List.of(ErrorDetail.of(ex.getParameterName(), null, reason));
		log.error("MissingServletRequestParameterException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.MISSING_REQUIRED_FIELD, ErrorCode.MISSING_REQUIRED_FIELD.getMessage(), errorDetails);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		List<ErrorDetail> errorDetails = List.of(ErrorDetail.of("requestBody", null,
			"요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요."));
		log.error("HttpMessageNotReadableException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.VALIDATION_FAILED, ErrorCode.VALIDATION_FAILED.getMessage(), errorDetails);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException ex) {
		String message = String.format("지원하지 않는 HTTP 메서드입니다: %s", ex.getMethod());
		log.error("HttpRequestMethodNotSupportedException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.VALIDATION_FAILED, message);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
		HttpMediaTypeNotSupportedException ex) {
		String message = String.format("지원하지 않는 Content-Type입니다: %s", ex.getContentType());
		log.error("HttpMediaTypeNotSupportedException caught in GlobalExceptionHandler", ex);
		return respond(ErrorCode.VALIDATION_FAILED, message);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
		log.error("ValidationException in GlobalExceptionHandler", ex);
		return respond(ex.getErrorCode(), ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> respond(ErrorCode errorCode, String message) {
		return respond(errorCode, message, Collections.emptyList());
	}

	private ResponseEntity<ErrorResponse> respond(ErrorCode errorCode, String message, List<ErrorDetail> errors) {
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), message, errors);
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(errorResponse);
	}

	private List<ErrorDetail> toErrorDetails(BindingResult bindingResult) {
		if (bindingResult == null || bindingResult.getFieldErrors().isEmpty()) {
			return Collections.emptyList();
		}
		return bindingResult.getFieldErrors().stream()
			.map(this::toErrorDetail)
			.toList();
	}

	private ErrorDetail toErrorDetail(FieldError fieldError) {
		String rejectedValue = fieldError.getRejectedValue() == null ? null : String.valueOf(fieldError.getRejectedValue());
		String reason = fieldError.getDefaultMessage();
		return ErrorDetail.of(fieldError.getField(), rejectedValue, reason);
	}
}
