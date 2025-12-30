package com.line7studio.boulderside.common.security.constants;

public final class SecurityWhitelist {
	private SecurityWhitelist() {
	}

	public static final String[] SWAGGER = {
		"/swagger-ui/**", "/v3/api-docs/**", "/"
	};

	public static final String[] PUBLIC = {
		"/api/auth/**", "/api/users/nickname/availability", "index.html"
	};

	public static final String[] ADMIN_PUBLIC = {
		"/admin/*.html",
		"/admin/js/**",
		"/admin/css/**",
		"/admin/images/**"
	};

	public static final String[] ADMIN = {
		"/api/admin/**"
	};
}
