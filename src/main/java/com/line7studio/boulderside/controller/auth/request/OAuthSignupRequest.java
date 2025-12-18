package com.line7studio.boulderside.controller.auth.request;

import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OAuthSignupRequest(
	@NotNull AuthProviderType providerType,
	@NotBlank String identityToken,
	@NotBlank String nickname,
	
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
