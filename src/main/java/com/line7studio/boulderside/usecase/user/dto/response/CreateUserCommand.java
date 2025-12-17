package com.line7studio.boulderside.usecase.user.dto.response;

import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import com.line7studio.boulderside.domain.user.enums.UserRole;
import lombok.Builder;

@Builder
public record CreateUserCommand(
	String nickname,
	AuthProviderType providerType,
	String providerUserId,
	String providerEmail,
	UserRole userRole,
	boolean privacyAgreed,
	boolean serviceTermsAgreed,
	boolean overFourteenAgreed,
	boolean marketingAgreed
) {
}
