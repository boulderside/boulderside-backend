package com.line7studio.boulderside.domain.feature.user.service;

import java.util.List;

import com.line7studio.boulderside.application.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;

public interface UserService {
	User getUserById(Long userId);

	List<User> findAllById(List<Long> userIdList);

	User createUser(CreateUserCommand createUserCommand);

	void updateUserProfileImage(Long userId, String profileImageUrl);

	void updateNickname(Long userId, String nickname);

	boolean isNicknameAvailable(String nickname);

	List<User> getAllUsers();

	User updateUserRole(Long userId, UserRole userRole);

}
