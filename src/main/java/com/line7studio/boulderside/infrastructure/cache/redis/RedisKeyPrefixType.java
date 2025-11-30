package com.line7studio.boulderside.infrastructure.cache.redis;

public enum RedisKeyPrefixType {
	// {도메인}:{서브도메인/용도}:{구체값} Key 네이밍 규칙
	PHONE_AUTH("phone:auth:");

	private final String prefix;

	RedisKeyPrefixType(String prefix) {
		this.prefix = prefix;
	}

	public String of(String value) {
		return prefix + value;
	}
}
