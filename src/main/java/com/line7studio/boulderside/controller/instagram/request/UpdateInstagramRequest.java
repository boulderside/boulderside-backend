package com.line7studio.boulderside.controller.instagram.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateInstagramRequest(
	@NotBlank(message = "Instagram URL은 필수입니다.")
	@Size(max = 500, message = "URL은 500자를 초과할 수 없습니다.")
	String url,
	List<Long> routeIds
) {
}