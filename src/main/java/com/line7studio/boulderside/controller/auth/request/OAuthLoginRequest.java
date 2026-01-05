package com.line7studio.boulderside.controller.auth.request;

import com.line7studio.boulderside.domain.user.enums.AuthProviderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OAuthLoginRequest(
	@NotNull(message = "OAuth 제공자는 필수입니다.")
	AuthProviderType providerType,

	@NotBlank(message = "Identity Token은 필수입니다.")
	@Size(min = 1, max = 2000, message = "Identity Token은 1자 이상 2000자 이하여야 합니다.")
	String identityToken
) {
}
