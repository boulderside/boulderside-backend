package com.line7studio.boulderside.usecase.auth.oauth;

import com.line7studio.boulderside.domain.feature.user.enums.AuthProviderType;

public record OAuthUserProfile(
	AuthProviderType providerType,
	String providerUserId
) {
}
