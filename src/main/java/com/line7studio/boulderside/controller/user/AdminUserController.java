package com.line7studio.boulderside.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.usecase.user.UserUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.user.request.UpdateUserRoleRequest;
import com.line7studio.boulderside.controller.user.request.UpdateUserStatusRequest;
import com.line7studio.boulderside.controller.user.response.AdminUserResponse;
import com.line7studio.boulderside.controller.user.response.UserConsentHistoryResponse;
import com.line7studio.boulderside.controller.user.response.UserLoginHistoryResponse;
import com.line7studio.boulderside.controller.user.response.UserMetaResponse;
import com.line7studio.boulderside.controller.user.response.UserStatusHistoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

	private final UserUseCase userUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<List<AdminUserResponse>>> getAllUsers() {
		List<AdminUserResponse> users = userUseCase.getAllUsers();
		return ResponseEntity.ok(ApiResponse.of(users));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<AdminUserResponse>> getUser(@PathVariable Long userId) {
		AdminUserResponse response = userUseCase.getUser(userId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/{userId}/meta")
	public ResponseEntity<ApiResponse<UserMetaResponse>> getUserMeta(@PathVariable Long userId) {
		UserMetaResponse response = userUseCase.getUserMeta(userId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/{userId}/consent-history")
	public ResponseEntity<ApiResponse<List<UserConsentHistoryResponse>>> getUserConsentHistory(@PathVariable Long userId) {
		List<UserConsentHistoryResponse> responses = userUseCase.getUserConsentHistory(userId);
		return ResponseEntity.ok(ApiResponse.of(responses));
	}

	@GetMapping("/{userId}/login-history")
	public ResponseEntity<ApiResponse<List<UserLoginHistoryResponse>>> getUserLoginHistory(@PathVariable Long userId) {
		List<UserLoginHistoryResponse> responses = userUseCase.getUserLoginHistory(userId);
		return ResponseEntity.ok(ApiResponse.of(responses));
	}

	@GetMapping("/{userId}/status-history")
	public ResponseEntity<ApiResponse<List<UserStatusHistoryResponse>>> getUserStatusHistory(@PathVariable Long userId) {
		List<UserStatusHistoryResponse> responses = userUseCase.getUserStatusHistory(userId);
		return ResponseEntity.ok(ApiResponse.of(responses));
	}

	@PutMapping("/{userId}/role")
	public ResponseEntity<ApiResponse<AdminUserResponse>> updateUserRole(
		@PathVariable Long userId,
		@Valid @RequestBody UpdateUserRoleRequest request) {
		AdminUserResponse response = userUseCase.updateUserRole(userId, request.userRole());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PutMapping("/{userId}/status")
	public ResponseEntity<ApiResponse<AdminUserResponse>> updateUserStatus(
		@PathVariable Long userId,
		@Valid @RequestBody UpdateUserStatusRequest request,
		@AuthenticationPrincipal CustomUserDetails adminUserDetails
	) {
		Long adminUserId = adminUserDetails != null ? adminUserDetails.userId() : null;
		AdminUserResponse response = userUseCase.updateUserStatus(userId, request, adminUserId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
