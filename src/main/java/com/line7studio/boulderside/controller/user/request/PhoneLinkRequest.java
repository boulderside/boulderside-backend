package com.line7studio.boulderside.controller.user.request;

import jakarta.validation.constraints.NotBlank;

public record PhoneLinkRequest(
	@NotBlank String phoneNumber,
	@NotBlank String email,
	@NotBlank String password
) {
}
