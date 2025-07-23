package com.example.boulderside.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
	private T data;
	private boolean success;

	public static <T> ApiResponse<T> of() {
		return ApiResponse.<T>builder()
			.success(true)
			.data(null)
			.build();
	}

	public static <T> ApiResponse<T> of(T data) {
		return ApiResponse.<T>builder()
			.success(true)
			.data(data)
			.build();
	}
}
