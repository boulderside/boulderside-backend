package com.line7studio.boulderside.controller.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFcmTokenRequest(
	@NotBlank
	@Size(max = 512)
	String fcmToken
) {
}
