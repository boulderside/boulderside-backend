package com.line7studio.boulderside.domain.aggregate.user.service;

import java.util.List;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

public interface UserService {
	User getUserById(Long userId);

	User getUserByPhone(String phoneNumber);

	List<User> findAllById(List<Long> userIdList);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}
