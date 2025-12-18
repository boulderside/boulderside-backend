package com.line7studio.boulderside.usecase.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.controller.user.response.AdminUserResponse;
import com.line7studio.boulderside.controller.user.response.UserConsentHistoryResponse;
import com.line7studio.boulderside.controller.user.response.UserLoginHistoryResponse;
import com.line7studio.boulderside.controller.user.response.UserMetaResponse;
import com.line7studio.boulderside.controller.user.response.UserStatusHistoryResponse;
import com.line7studio.boulderside.controller.user.request.UpdateUserStatusRequest;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.enums.UserRole;
import com.line7studio.boulderside.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserUseCase {
	private final UserService userService;

	@Transactional(readOnly = true)
	public List<AdminUserResponse> getAllUsers() {
		return userService.getAllUsers()
			.stream()
			.map(AdminUserResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public AdminUserResponse getUser(Long userId) {
		return AdminUserResponse.from(userService.getUserById(userId));
	}

	@Transactional(readOnly = true)
	public UserMetaResponse getUserMeta(Long userId) {
		return UserMetaResponse.from(userService.getUserMeta(userId));
	}

	@Transactional(readOnly = true)
	public List<UserConsentHistoryResponse> getUserConsentHistory(Long userId) {
		return userService.getUserConsentHistories(userId).stream()
			.map(UserConsentHistoryResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<UserLoginHistoryResponse> getUserLoginHistory(Long userId) {
		return userService.getUserLoginHistories(userId).stream()
			.map(UserLoginHistoryResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<UserStatusHistoryResponse> getUserStatusHistory(Long userId) {
		return userService.getUserStatusHistories(userId).stream()
			.map(UserStatusHistoryResponse::from)
			.toList();
	}

	@Transactional
	public AdminUserResponse updateUserRole(Long userId, UserRole userRole) {
		User updatedUser = userService.updateUserRole(userId, userRole);
		return AdminUserResponse.from(updatedUser);
	}

	@Transactional
	public AdminUserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request, Long adminUserId) {
		User updatedUser = userService.updateUserStatus(
			userId,
			request.userStatus(),
			request.reasonType(),
			request.reasonDetail(),
			adminUserId
		);
		return AdminUserResponse.from(updatedUser);
	}
}
