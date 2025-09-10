package com.line7studio.boulderside.controller.user.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
	@NotBlank(message = "휴대폰 번호는 필수입니다")
	String phoneNumber,

	@NotBlank(message = "새 비밀번호는 필수입니다")
	String newPassword
) {
}
