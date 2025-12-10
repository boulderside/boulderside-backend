package com.line7studio.boulderside.infrastructure.oauth;

import com.line7studio.boulderside.application.auth.oauth.OAuthClient;
import com.line7studio.boulderside.application.auth.oauth.OAuthUserProfile;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.user.enums.AuthProviderType;

import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class KakaoOAuthClient implements OAuthClient {
	private final WebClient webClient;

	public KakaoOAuthClient(
		WebClient.Builder webClientBuilder,
		@Value("${oauth.kakao.base-url:https://kapi.kakao.com}") String kakaoBaseUrl
	) {
		this.webClient = webClientBuilder
			.baseUrl(kakaoBaseUrl)
			.build();
	}

	@Override
	public AuthProviderType providerType() {
		return AuthProviderType.KAKAO;
	}

	@Override
	public OAuthUserProfile fetchUserProfile(String accessToken) {
		KakaoUserResponse response = webClient.get()
			.uri("/v2/user/me")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.onStatus(HttpStatusCode::isError,
				clientResponse -> Mono.error(new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID)))
			.bodyToMono(KakaoUserResponse.class)
			.blockOptional()
			.orElseThrow(() -> new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID));

		String providerUserId = String.valueOf(response.id());

		return new OAuthUserProfile(
			AuthProviderType.KAKAO,
			providerUserId
		);
	}

	private record KakaoUserResponse(Long id) {
	}
}
