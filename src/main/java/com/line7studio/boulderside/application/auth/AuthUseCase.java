package com.line7studio.boulderside.application.auth;

import com.line7studio.boulderside.application.auth.oauth.OAuthClient;
import com.line7studio.boulderside.application.auth.oauth.OAuthClientRegistry;
import com.line7studio.boulderside.application.auth.oauth.OAuthUserProfile;
import com.line7studio.boulderside.application.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.security.provider.TokenProvider;
import com.line7studio.boulderside.common.security.vo.LoginResponse;
import com.line7studio.boulderside.controller.auth.request.OAuthLoginRequest;
import com.line7studio.boulderside.controller.auth.request.OAuthSignupRequest;
import com.line7studio.boulderside.controller.auth.request.RefreshTokenRequest;
import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import com.line7studio.boulderside.domain.feature.user.service.UserCredentialService;
import com.line7studio.boulderside.domain.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthUseCase {
	private final OAuthClientRegistry oAuthClientRegistry;
	private final UserService userService;
	private final UserCredentialService userCredentialService;
	private final TokenProvider tokenProvider;

	@Transactional
	public LoginResponse loginWithOAuth(OAuthLoginRequest request) {
		OAuthClient client = oAuthClientRegistry.getClient(request.providerType());
		OAuthUserProfile profile = client.fetchUserProfile(request.identityToken());

		var credential = userCredentialService.findByProvider(request.providerType(), profile.providerUserId())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_REGISTERED));

		User user = userService.getUserById(credential.getUserId());

		String accessToken = tokenProvider.create("access", user.getId(), user.getUserRole());
		String refreshToken = tokenProvider.create("refresh", user.getId(), user.getUserRole());
		userCredentialService.updateOAuthCredential(credential, refreshToken);

		return LoginResponse.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.isNew(false)
			.build();
	}

	@Transactional
	public LoginResponse signupWithOAuth(OAuthSignupRequest request) {
		OAuthClient client = oAuthClientRegistry.getClient(request.providerType());
		OAuthUserProfile profile = client.fetchUserProfile(request.identityToken());

		if (userCredentialService.findByProvider(request.providerType(), profile.providerUserId()).isPresent()) {
			throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
		}

		CreateUserCommand command = CreateUserCommand.builder()
			.nickname(request.nickname())
			.userRole(UserRole.ROLE_USER)
			.build();

		User user = userService.createUser(command);

		String accessToken = tokenProvider.create("access", user.getId(), user.getUserRole());
		String refreshToken = tokenProvider.create("refresh", user.getId(), user.getUserRole());

		userCredentialService.createOAuthCredential(
			user,
			request.providerType(),
			profile.providerUserId(),
			refreshToken
		);

		return LoginResponse.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.isNew(true)
			.build();
	}

	@Transactional
	public LoginResponse refreshTokens(RefreshTokenRequest request) {
		String providedRefreshToken = request.refreshToken();
		validateRefreshToken(providedRefreshToken);

		Long userId = tokenProvider.getUserId(providedRefreshToken);
		var credential = userCredentialService.findByUserId(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID));

		if (!providedRefreshToken.equals(credential.getRefreshToken())) {
			throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
		}

		User user = userService.getUserById(userId);

		String newAccessToken = tokenProvider.create("access", userId, user.getUserRole());
		String newRefreshToken = tokenProvider.create("refresh", userId, user.getUserRole());
		userCredentialService.updateOAuthCredential(credential, newRefreshToken);

		return LoginResponse.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.accessToken(newAccessToken)
			.refreshToken(newRefreshToken)
			.isNew(false)
			.build();
	}

	private void validateRefreshToken(String refreshToken) {
		try {
			String category = tokenProvider.getCategory(refreshToken);
			boolean expired = tokenProvider.isExpired(refreshToken);
			if (!"refresh".equals(category) || expired) {
				throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
			}
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
		}
	}
}
