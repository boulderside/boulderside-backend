package com.line7studio.boulderside.common.security.constants;

public final class SecurityWhitelist {
	private SecurityWhitelist() {
	}

	public static final String[] SWAGGER = {
		"/swagger-ui/**", "/v3/api-docs/**", "/"
	};

	public static final String[] PUBLIC = {
		"users/check-id", "users/phone/send-code", "users/phone/verify-code", "users/phone/link-account",
		"users/phone/lookup", "users/phone/link-account", "users/sign-up", "users/find-id",
		"users/change-password"
	};
}
