package com.line7studio.boulderside.domain.user.enums;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AuthProviderType {
	APPLE,
	NAVER,
	KAKAO,
	GOOGLE;

	public static AuthProviderType from(String value) {
		return Arrays.stream(values())
			.filter(it -> it.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_SUPPORTED_AUTH_PROVIDER));
	}
}
