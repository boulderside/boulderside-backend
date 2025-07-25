package com.example.boulderside.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
	private String errorCode;
	private String errorMessage;

	public static ErrorResponse of(String errorCode, String errorMessage) {
		return ErrorResponse.builder()
			.errorCode(errorCode)
			.errorMessage(errorMessage)
			.build();
	}
}
