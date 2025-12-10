package com.line7studio.boulderside.controller.auth.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
	@NotBlank String refreshToken
) {
}
