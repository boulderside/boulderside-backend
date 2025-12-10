//package com.line7studio.boulderside.infrastructure.oauth;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import com.line7studio.boulderside.application.auth.oauth.OAuthClient;
//import com.line7studio.boulderside.application.auth.oauth.OAuthUserProfile;
//import com.line7studio.boulderside.common.exception.BusinessException;
//import com.line7studio.boulderside.common.exception.ErrorCode;
//import com.line7studio.boulderside.domain.feature.user.enums.AuthProviderType;
//
//import reactor.core.publisher.Mono;
//
//@Component
//public class GoogleOAuthClient implements OAuthClient {
//	private final WebClient webClient;
//	private final String clientId;
//
//	public GoogleOAuthClient(WebClient.Builder webClientBuilder,
//		@Value("${oauth.google.client-id:}") String clientId) {
//		this.webClient = webClientBuilder
//			.baseUrl("https://oauth2.googleapis.com")
//			.build();
//		this.clientId = clientId;
//	}
//
//	@Override
//	public AuthProviderType providerType() {
//		return AuthProviderType.GOOGLE;
//	}
//
//	@Override
//	public OAuthUserProfile fetchUserProfile(String identityToken) {
//		GoogleTokenInfoResponse response = webClient.get()
//			.uri(uriBuilder -> uriBuilder.path("/tokeninfo")
//				.queryParam("id_token", identityToken)
//				.build())
//			.retrieve()
//			.onStatus(HttpStatusCode::isError,
//				clientResponse -> Mono.error(new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID)))
//			.bodyToMono(GoogleTokenInfoResponse.class)
//			.blockOptional()
//			.orElseThrow(() -> new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID));
//
//		if (clientId != null && !clientId.isBlank() && !clientId.equals(response.aud())) {
//			throw new BusinessException(ErrorCode.OAUTH_TOKEN_INVALID);
//		}
//
//		return new OAuthUserProfile(
//			AuthProviderType.GOOGLE,
//			response.sub(),
//			response.email(),
//			response.name(),
//			response.picture()
//		);
//	}
//
//	private record GoogleTokenInfoResponse(
//		String sub,
//		String email,
//		String name,
//		String picture,
//		String aud
//	) {
//	}
//}
