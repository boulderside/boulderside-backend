package com.line7studio.boulderside.common.security.vo;

import lombok.Builder;

@Builder
public record LoginResponse(String email, String nickname, String accessToken, String refreshToken) {
}
