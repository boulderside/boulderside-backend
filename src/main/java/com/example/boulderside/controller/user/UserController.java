package com.example.boulderside.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boulderside.application.user.UserUseCase;
import com.example.boulderside.application.user.dto.response.UserInfoResponse;
import com.example.boulderside.common.response.ApiResponse;
import com.example.boulderside.common.security.details.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserUseCase userUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		UserInfoResponse userInfoResponse = userUseCase.getUserInfo(userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(userInfoResponse));
	}
}
