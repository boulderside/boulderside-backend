package com.line7studio.boulderside.usecase.auth.oauth;

import com.line7studio.boulderside.domain.user.enums.AuthProviderType;

public interface OAuthClient {
	AuthProviderType providerType();

	OAuthUserProfile fetchUserProfile(String identityToken);
}
