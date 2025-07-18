package com.example.boulderside.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.boulderside.common.error.ErrorCode;
import com.example.boulderside.common.exception.ApplicationException;
import com.example.boulderside.common.exception.DatabaseException;
import com.example.boulderside.common.exception.DomainException;
import com.example.boulderside.common.exception.ExternalApiException;
import com.example.boulderside.common.exception.ValidationException;
import com.example.boulderside.controller.dto.response.ErrorResponse;

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

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
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
