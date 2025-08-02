package com.line7studio.boulderside.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
	private T data;
	private Boolean success;

	public static <T> ApiResponse<T> success() {
		return ApiResponse.<T>builder()
			.success(true)
			.build();
	}

	public static <T> ApiResponse<T> of(T data) {
		return ApiResponse.<T>builder()
			.data(data)
			.build();
	}
}
