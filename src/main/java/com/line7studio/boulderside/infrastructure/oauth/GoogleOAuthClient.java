package com.line7studio.boulderside.infrastructure.oauth;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import com.line7studio.boulderside.usecase.auth.oauth.OAuthClient;
import com.line7studio.boulderside.usecase.auth.oauth.OAuthUserProfile;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthClient implements OAuthClient {
	private static final String DEFAULT_JWK_SET_URI = "https://www.googleapis.com/oauth2/v3/certs";
	private static final List<String> ALLOWED_ISSUERS = List.of(
		"https://accounts.google.com",
		"accounts.google.com"
	);

	private final JwtDecoder jwtDecoder;
	private final Set<String> allowedClientIds;

	public GoogleOAuthClient(
		@Value("${oauth.google.jwk-set-uri:" + DEFAULT_JWK_SET_URI + "}") String jwkSetUri,
		@Value("${oauth.google.client-ids:}") String clientIds,
		@Value("${oauth.google.client-id:}") String clientId
	) {
		this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
		this.allowedClientIds = buildAllowedClientIds(clientIds, clientId);
	}

	@Override
	public AuthProviderType providerType() {
		return AuthProviderType.GOOGLE;
	}

	@Override
	public OAuthUserProfile fetchUserProfile(String identityToken) {
		Jwt jwt = decodeToken(identityToken);
		validateIssuer(jwt);
		validateAudience(jwt);

		String subject = jwt.getSubject();
		if (subject == null || subject.isBlank()) {
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}

		return new OAuthUserProfile(AuthProviderType.GOOGLE, subject);
	}

	private Jwt decodeToken(String identityToken) {
		try {
			return jwtDecoder.decode(identityToken);
		} catch (JwtException ex) {
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}
	}

	private void validateIssuer(Jwt jwt) {
		String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
		if (issuer == null || !ALLOWED_ISSUERS.contains(issuer)) {
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}
	}

	private void validateAudience(Jwt jwt) {
		if (allowedClientIds.isEmpty()) {
			return;
		}

		List<String> audiences = jwt.getAudience();
		boolean matched = audiences != null && audiences.stream().anyMatch(allowedClientIds::contains);
		if (!matched) {
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}
	}

	private Set<String> buildAllowedClientIds(String clientIdsRaw, String clientIdRaw) {
		Set<String> clientIds = new LinkedHashSet<>();
		clientIds.addAll(parseClientIds(clientIdsRaw));
		clientIds.addAll(parseClientIds(clientIdRaw));
		return Collections.unmodifiableSet(clientIds);
	}

	private Set<String> parseClientIds(String raw) {
		if (raw == null || raw.isBlank()) {
			return Collections.emptySet();
		}
		return Arrays.stream(raw.split(","))
			.map(String::trim)
			.filter(value -> !value.isEmpty())
			.collect(java.util.stream.Collectors.toUnmodifiableSet());
	}
}
