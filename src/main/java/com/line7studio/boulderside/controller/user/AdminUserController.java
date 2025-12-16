package com.line7studio.boulderside.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.user.UserUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.user.request.UpdateUserRoleRequest;
import com.line7studio.boulderside.controller.user.response.AdminUserResponse;

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

	@PutMapping("/{userId}/role")
	public ResponseEntity<ApiResponse<AdminUserResponse>> updateUserRole(
		@PathVariable Long userId,
		@Valid @RequestBody UpdateUserRoleRequest request) {
		AdminUserResponse response = userUseCase.updateUserRole(userId, request.userRole());
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
