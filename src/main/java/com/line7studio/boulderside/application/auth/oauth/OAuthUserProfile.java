package com.line7studio.boulderside.application.auth.oauth;

import com.line7studio.boulderside.domain.feature.user.enums.AuthProviderType;

public record OAuthUserProfile(
	AuthProviderType providerType,
	String providerUserId
) {
}
