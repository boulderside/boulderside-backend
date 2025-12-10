package com.line7studio.boulderside.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.auth.AuthUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.vo.LoginResponse;
import com.line7studio.boulderside.controller.auth.request.OAuthLoginRequest;
import com.line7studio.boulderside.controller.auth.request.RefreshTokenRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	private final AuthUseCase authUseCase;

	@PostMapping("/oauth/login")
	public ResponseEntity<ApiResponse<LoginResponse>> loginWithOAuth(
		@Valid @RequestBody OAuthLoginRequest request) {
        LoginResponse response = authUseCase.loginWithOAuth(request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/oauth/refresh")
	public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
		@Valid @RequestBody RefreshTokenRequest request) {
		LoginResponse response = authUseCase.refreshTokens(request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
