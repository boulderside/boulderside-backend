package com.line7studio.boulderside.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.user.UserUseCase;
import com.line7studio.boulderside.application.user.dto.response.UserInfoResponse;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserUseCase userUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		System.out.println(userDetails.getUsername());
		UserInfoResponse userInfoResponse = userUseCase.getUserInfo(userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(userInfoResponse));
	}
}
