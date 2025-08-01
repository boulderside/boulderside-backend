package com.line7studio.boulderside.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.user.UserUseCase;
import com.line7studio.boulderside.application.user.dto.response.UserInfoResponse;
import com.line7studio.boulderside.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserUseCase userUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUser() {
		UserInfoResponse userInfoResponse = userUseCase.getUserInfo(10L);
		return ResponseEntity.ok(ApiResponse.of(userInfoResponse));
	}
}
