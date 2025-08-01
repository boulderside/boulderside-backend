package com.line7studio.boulderside.common.security.vo;

public record LoginResponse(String email, String nickname, String accessToken, String refreshToken) {
}
