package com.line7studio.boulderside.infrastructure.oauth;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import com.line7studio.boulderside.usecase.auth.oauth.OAuthClient;
import com.line7studio.boulderside.usecase.auth.oauth.OAuthUserProfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class AppleOAuthClient implements OAuthClient {
	private static final String APPLE_ISSUER = "https://appleid.apple.com";

	private final List<String> clientIds;
	private final PrivateKey privateKey;
	private final JwtDecoder jwtDecoder;
	private final ResourceLoader resourceLoader;

	public AppleOAuthClient(
		@Value("${oauth.apple.client-ids}") String clientIds,
		@Value("${oauth.apple.private-key-path}") String privateKeyPath,
		@Value("${oauth.apple.jwk-set-uri:https://appleid.apple.com/auth/keys}") String jwkSetUri,
		ResourceLoader resourceLoader
	) {
		this.clientIds = List.of(clientIds.split(","));
		this.resourceLoader = resourceLoader;
		this.privateKey = loadPrivateKey(privateKeyPath);
		this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	@Override
	public AuthProviderType providerType() {
		return AuthProviderType.APPLE;
	}

	@Override
	public OAuthUserProfile fetchUserProfile(String identityToken) {
		log.info("Received Apple identity token: {}", identityToken);

		// 1. identityToken 검증
		Jwt jwt = decodeAndValidateToken(identityToken);

		// 2. subject 추출 (Apple User ID)
		String subject = jwt.getSubject();
		if (subject == null || subject.isBlank()) {
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}

		return new OAuthUserProfile(AuthProviderType.APPLE, subject);
	}

	/**
	 * identityToken을 검증하고 디코딩합니다.
	 */
	private Jwt decodeAndValidateToken(String identityToken) {
		try {
			Jwt jwt = jwtDecoder.decode(identityToken);
			log.info("Decoded JWT Claims: {}", jwt.getClaims());
			log.info("JWT Issuer: {}", jwt.getIssuer());
			log.info("JWT Subject: {}", jwt.getSubject());
			log.info("JWT Audience: {}", jwt.getAudience());
			log.info("JWT Issued At: {}", jwt.getIssuedAt());
			log.info("JWT Expires At: {}", jwt.getExpiresAt());
			validateIssuer(jwt);
			validateAudience(jwt);
			return jwt;
		} catch (JwtException ex) {
			log.error("Apple identity token validation failed", ex);
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}
	}

	/**
	 * JWT의 issuer를 검증합니다.
	 */
	private void validateIssuer(Jwt jwt) {
		String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
		if (!APPLE_ISSUER.equals(issuer)) {
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}
	}

	/**
	 * JWT의 audience를 검증합니다.
	 */
	private void validateAudience(Jwt jwt) {
		List<String> audiences = jwt.getAudience();
		log.info("JWT audiences: {}", audiences);
		log.info("Expected clientIds: {}", clientIds);

		if (audiences == null || audiences.isEmpty()) {
			log.error("No audience found in JWT");
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}

		boolean matched = audiences.stream()
			.anyMatch(clientIds::contains);

		if (!matched) {
			log.error("Audience validation failed. JWT audiences: {}, Expected clientIds: {}", audiences, clientIds);
			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
		}
	}

	/**
	 * .p8 private key 파일을 로드합니다.
	 */
	private PrivateKey loadPrivateKey(String privateKeyPath) {
		try {
			Resource resource = resourceLoader.getResource(privateKeyPath);
			String privateKeyContent = new String(resource.getInputStream().readAllBytes());

			// Remove header, footer, and whitespace
			privateKeyContent = privateKeyContent
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s", "");

			byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("EC");

			return keyFactory.generatePrivate(keySpec);
		} catch (IOException e) {
			log.error("Failed to read Apple private key file: {}", privateKeyPath, e);
			throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
		} catch (Exception e) {
			log.error("Failed to load Apple private key", e);
			throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
		}
	}

}
