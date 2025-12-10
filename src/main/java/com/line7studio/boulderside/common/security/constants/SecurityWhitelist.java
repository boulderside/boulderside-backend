package com.line7studio.boulderside.common.security.constants;

public final class SecurityWhitelist {
	private SecurityWhitelist() {
	}

	public static final String[] SWAGGER = {
		"/swagger-ui/**", "/v3/api-docs/**", "/"
	};

	public static final String[] PUBLIC = {
		"/auth/**", "/users/nickname/availability"
	};

    public static final String[] ADMIN_PUBLIC = {
            "/admin/**"
    };

	public static final String[] ADMIN = {
        "/api/admin/**"
	};
}
