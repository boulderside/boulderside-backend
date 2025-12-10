package com.line7studio.boulderside.common.security.vo;

import lombok.Builder;

@Builder
public record LoginResponse(Long userId, String nickname, String accessToken, String refreshToken, boolean isNew) {
}
