package com.line7studio.boulderside.usecase.auth;

import com.line7studio.boulderside.domain.user.UserLoginHistory;
import com.line7studio.boulderside.domain.user.repository.UserLoginHistoryRepository;
import com.line7studio.boulderside.usecase.auth.oauth.OAuthClient;
import com.line7studio.boulderside.usecase.auth.oauth.OAuthClientRegistry;
import com.line7studio.boulderside.usecase.auth.oauth.OAuthUserProfile;
import com.line7studio.boulderside.usecase.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.security.provider.TokenProvider;
import com.line7studio.boulderside.common.security.vo.LoginResponse;
import com.line7studio.boulderside.controller.auth.request.OAuthLoginRequest;
import com.line7studio.boulderside.controller.auth.request.OAuthSignupRequest;
import com.line7studio.boulderside.controller.auth.request.RefreshTokenRequest;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.enums.UserRole;
import com.line7studio.boulderside.domain.user.enums.UserStatus;
import com.line7studio.boulderside.domain.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUseCase {
	private final OAuthClientRegistry oAuthClientRegistry;
	private final UserService userService;
	private final TokenProvider tokenProvider;
	private final UserLoginHistoryRepository userLoginHistoryRepository;

	private static final long WITHDRAWAL_REJOIN_COOLDOWN_DAYS = 30L;

	@Value("${admin.oauth.provider-user-ids:}")
	private String adminProviderUserIdsRaw;

	private Set<String> adminProviderUserIds = Collections.emptySet();

	@PostConstruct
	void initAdminProviderUserIds() {
		adminProviderUserIds = parseAdminProviderUserIds(adminProviderUserIdsRaw);
	}

	@Transactional
	public LoginResponse loginWithOAuth(OAuthLoginRequest request, String ipAddress, String userAgent) {
		OAuthClient client = oAuthClientRegistry.getClient(request.providerType());
		OAuthUserProfile profile = client.fetchUserProfile(request.identityToken());
		boolean isAdminProviderUserId = isAdminProviderUserId(profile.providerUserId());

		User user = userService.findByProvider(request.providerType(), profile.providerUserId())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_REGISTERED));

		if (isAdminProviderUserId && user.getUserRole() != UserRole.ROLE_ADMIN) {
			user = userService.updateUserRole(user.getId(), UserRole.ROLE_ADMIN);
		}

		validateLoginStatus(user);

		String accessToken = tokenProvider.create("access", user.getId(), user.getUserRole());
		String refreshToken = tokenProvider.create("refresh", user.getId(), user.getUserRole());
		userService.updateRefreshToken(user.getId(), refreshToken);

		saveLoginHistory(user.getId(), ipAddress, userAgent);

		return LoginResponse.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.isNew(false)
			.build();
	}

	@Transactional
	public LoginResponse signupWithOAuth(OAuthSignupRequest request, String ipAddress, String userAgent) {
		OAuthClient client = oAuthClientRegistry.getClient(request.providerType());
		OAuthUserProfile profile = client.fetchUserProfile(request.identityToken());
		boolean isAdminProviderUserId = isAdminProviderUserId(profile.providerUserId());

		userService.findByProvider(request.providerType(), profile.providerUserId())
			.ifPresent(this::validateSignupAvailability);

		CreateUserCommand command = CreateUserCommand.builder()
			.nickname(request.nickname())
			.providerType(profile.providerType())
			.providerUserId(profile.providerUserId())
			.providerEmail(null)
			.userRole(isAdminProviderUserId ? UserRole.ROLE_ADMIN : UserRole.ROLE_USER)
			.privacyAgreed(request.privacyAgreed())
			.serviceTermsAgreed(request.serviceTermsAgreed())
			.overFourteenAgreed(request.overFourteenAgreed())
			.marketingAgreed(Boolean.TRUE.equals(request.marketingAgreed()))
			.build();

		User user = userService.createUser(command);

		String accessToken = tokenProvider.create("access", user.getId(), user.getUserRole());
		String refreshToken = tokenProvider.create("refresh", user.getId(), user.getUserRole());

		userService.updateRefreshToken(user.getId(), refreshToken);

		saveLoginHistory(user.getId(), ipAddress, userAgent);

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
		User user = userService.getUserById(userId);

		validateLoginStatus(user);

		if (user.getRefreshToken() == null || !providedRefreshToken.equals(user.getRefreshToken())) {
			throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
		}

		String newAccessToken = tokenProvider.create("access", userId, user.getUserRole());
		String newRefreshToken = tokenProvider.create("refresh", userId, user.getUserRole());
		userService.updateRefreshToken(userId, newRefreshToken);

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

	private void saveLoginHistory(Long userId, String ipAddress, String userAgent) {
		UserLoginHistory history = UserLoginHistory.builder()
			.userId(userId)
			.ipAddress(ipAddress)
			.userAgent(userAgent)
			.loginAt(LocalDateTime.now())
			.build();
		userLoginHistoryRepository.save(history);
	}

	private void validateSignupAvailability(User user) {
		UserStatus status = user.getUserStatus();
		switch (status) {
			case ACTIVE -> throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
			case PENDING -> throw new BusinessException(ErrorCode.USER_PENDING);
			case BANNED -> throw new BusinessException(ErrorCode.USER_BANNED);
			case INACTIVE -> {
				if (userService.isWithdrawnWithinDays(user.getId(), WITHDRAWAL_REJOIN_COOLDOWN_DAYS)) {
					throw new BusinessException(ErrorCode.USER_WITHDRAWAL_COOLDOWN);
				}
				throw new BusinessException(ErrorCode.USER_INACTIVE);
			}
			default -> throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
		}
	}

	private void validateLoginStatus(User user) {
		UserStatus status = user.getUserStatus();
		switch (status) {
			case ACTIVE -> { return; }
			case PENDING -> throw new BusinessException(ErrorCode.USER_PENDING);
			case INACTIVE -> throw new BusinessException(ErrorCode.USER_INACTIVE);
			case BANNED -> throw new BusinessException(ErrorCode.USER_BANNED);
			default -> throw new BusinessException(ErrorCode.USER_INACTIVE);
		}
	}

	private boolean isAdminProviderUserId(String providerUserId) {
		return providerUserId != null && adminProviderUserIds.contains(providerUserId);
	}

	private Set<String> parseAdminProviderUserIds(String raw) {
		if (raw == null || raw.isBlank()) {
			return Collections.emptySet();
		}
		return Arrays.stream(raw.split(","))
			.map(String::trim)
			.filter(value -> !value.isEmpty())
			.collect(Collectors.toUnmodifiableSet());
	}
}
