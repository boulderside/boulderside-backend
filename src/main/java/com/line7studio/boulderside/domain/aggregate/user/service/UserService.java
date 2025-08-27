package com.line7studio.boulderside.domain.aggregate.user.service;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

import java.util.List;

public interface UserService {
	User getUserById(Long userId);

	User getUserByPhone(String phoneNumber);

	boolean isUserIdDuplicate(String email);

  	List<User> findAllById(List<Long> userIdList);
	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}
