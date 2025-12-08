package com.line7studio.boulderside.domain.feature.user.service;

import java.util.List;

import com.line7studio.boulderside.application.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;

public interface UserService {
	User getUserById(Long userId);

	User getUserByPhone(String phoneNumber);

	void updateUserByPhone(String phoneNumber, String email, String password);

	List<User> findAllById(List<Long> userIdList);

	boolean existsByEmail(String email);

	User createUser(CreateUserCommand createUserCommand);

	void updateUserProfileImage(Long userId, String profileImageUrl);

	void validateUserNotExistsByPhone(String encodedPhoneNumber);

	User findUserByPhone(String phoneNumber);

	void updatePasswordByPhone(String phoneNumber, String newPassword);

	List<User> getAllUsers();

	User updateUserRole(Long userId, UserRole userRole);
}
