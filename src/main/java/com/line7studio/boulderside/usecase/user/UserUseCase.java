package com.line7studio.boulderside.usecase.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.controller.user.response.AdminUserResponse;
import com.line7studio.boulderside.domain.feature.user.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import com.line7studio.boulderside.domain.feature.user.service.UserService;

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

	@Transactional
	public AdminUserResponse updateUserRole(Long userId, UserRole userRole) {
		User updatedUser = userService.updateUserRole(userId, userRole);
		return AdminUserResponse.from(updatedUser);
	}
}
