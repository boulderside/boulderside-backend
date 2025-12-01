package com.line7studio.boulderside.common.response;

import com.line7studio.boulderside.common.exception.ErrorCode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
	private String code;
	private String message;
	@Builder.Default
	private List<ErrorDetail> errors = Collections.emptyList();

	public static ErrorResponse of(ErrorCode errorCode) {
		return of(errorCode, errorCode.getMessage());
	}

	public static ErrorResponse of(ErrorCode errorCode, String message) {
		return of(errorCode.getCode(), message);
	}

	public static ErrorResponse of(ErrorCode errorCode, List<ErrorDetail> errors) {
		return of(errorCode.getCode(), errorCode.getMessage(), errors);
	}

	public static ErrorResponse of(String code, String message) {
		return of(code, message, Collections.emptyList());
	}

	public static ErrorResponse of(String code, String message, List<ErrorDetail> errors) {
		return ErrorResponse.builder()
			.code(code)
			.message(message)
			.errors(Objects.requireNonNullElse(errors, Collections.emptyList()))
			.build();
	}
}
