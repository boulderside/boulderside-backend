package com.line7studio.boulderside.domain.aggregate.user.service;

import com.line7studio.boulderside.application.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;

import java.util.List;

public interface UserService {
	User getUserById(Long userId);

	User getUserByPhone(String phoneNumber);

	void updateUserByPhone(String phoneNumber, String email, String password);

	List<User> findAllById(List<Long> userIdList);

	boolean existsByEmail(String email);

	User createUser(CreateUserCommand createUserCommand);

	void updateUserProfileImage(Long userId, String profileImageUrl);

    void validateUserNotExistsByPhone(String encodedPhoneNumber);
}
