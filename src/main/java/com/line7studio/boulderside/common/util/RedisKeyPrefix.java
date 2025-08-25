package com.line7studio.boulderside.common.util;

public enum RedisKeyPrefix {
	// {도메인}:{서브도메인/용도}:{구체값} Key 네이밍 규칙
	PHONE_AUTH("phone:auth:");

	private final String prefix;

	RedisKeyPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String of(String value) {
		return prefix + value;
	}
}
