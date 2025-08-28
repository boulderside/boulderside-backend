package com.line7studio.boulderside.domain.aggregate.user.service;

import java.util.List;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserSex;

public interface UserService {
	User getUserById(Long userId);

	User getUserByPhone(String phoneNumber);

	User updateUserByPhone(String phoneNumber, String email, String password);

	List<User> findAllById(List<Long> userIdList);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	User createUser(String nickname, String profileImageUrl, String phone, UserRole userRole, UserSex userSex,
		Level userLevel, String name, String email, String password);
}
