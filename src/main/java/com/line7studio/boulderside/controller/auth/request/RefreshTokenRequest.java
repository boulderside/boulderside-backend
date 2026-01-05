package com.line7studio.boulderside.controller.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(
	@NotBlank(message = "리프레시 토큰은 필수입니다.")
	@Size(min = 1, max = 512, message = "리프레시 토큰은 1자 이상 512자 이하여야 합니다.")
	String refreshToken
) {
}
