package com.line7studio.boulderside.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.usecase.auth.AuthUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.vo.LoginResponse;
import com.line7studio.boulderside.controller.auth.request.OAuthLoginRequest;
import com.line7studio.boulderside.controller.auth.request.OAuthSignupRequest;
import com.line7studio.boulderside.controller.auth.request.RefreshTokenRequest;

import jakarta.servlet.http.HttpServletRequest;
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
		@Valid @RequestBody OAuthLoginRequest request,
		HttpServletRequest servletRequest) {
		String ipAddress = getClientIp(servletRequest);
		String userAgent = servletRequest.getHeader("User-Agent");
		LoginResponse response = authUseCase.loginWithOAuth(request, ipAddress, userAgent);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/oauth/signup")
	public ResponseEntity<ApiResponse<LoginResponse>> signupWithOAuth(
		@Valid @RequestBody OAuthSignupRequest request,
		HttpServletRequest servletRequest) {
		String ipAddress = getClientIp(servletRequest);
		String userAgent = servletRequest.getHeader("User-Agent");
		LoginResponse response = authUseCase.signupWithOAuth(request, ipAddress, userAgent);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/oauth/refresh")
	public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
		@Valid @RequestBody RefreshTokenRequest request) {
		LoginResponse response = authUseCase.refreshTokens(request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	private String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}