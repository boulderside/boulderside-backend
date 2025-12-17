package com.line7studio.boulderside.usecase.auth.oauth;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;

@Component
public class OAuthClientRegistry {
	private final Map<AuthProviderType, OAuthClient> clientMap = new EnumMap<>(AuthProviderType.class);

	public OAuthClientRegistry(List<OAuthClient> clients) {
		for (OAuthClient client : clients) {
			clientMap.put(client.providerType(), client);
		}
	}

	public OAuthClient getClient(AuthProviderType providerType) {
		OAuthClient client = clientMap.get(providerType);
		if (client == null) {
			throw new BusinessException(ErrorCode.NOT_SUPPORTED_AUTH_PROVIDER);
		}
		return client;
	}
}
