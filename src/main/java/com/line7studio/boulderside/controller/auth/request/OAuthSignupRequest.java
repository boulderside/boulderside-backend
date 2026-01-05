package com.line7studio.boulderside.controller.auth.request;

import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OAuthSignupRequest(
	@NotNull(message = "OAuth 제공자는 필수입니다.")
	AuthProviderType providerType,

	@NotBlank(message = "Identity Token은 필수입니다.")
	String identityToken,

	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
	String nickname,

	@NotNull
	@AssertTrue(message = "개인정보 수집 및 이용 동의는 필수입니다.")
	Boolean privacyAgreed,

	@NotNull
	@AssertTrue(message = "서비스 이용 약관 동의는 필수입니다.")
	Boolean serviceTermsAgreed,

	@NotNull
	@AssertTrue(message = "14세 이상 동의는 필수입니다.")
	Boolean overFourteenAgreed,

	Boolean marketingAgreed
) {
}
