package com.line7studio.boulderside.controller.auth.request;

import com.line7studio.boulderside.domain.user.enums.AuthProviderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OAuthLoginRequest(
	@NotNull AuthProviderType providerType,
	@NotBlank String identityToken
) {
}
