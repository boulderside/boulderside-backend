package com.line7studio.boulderside.controller.user.request;

import jakarta.validation.constraints.NotBlank;

public record FindIdByPhoneRequest(
	@NotBlank String phoneNumber
) {
}

