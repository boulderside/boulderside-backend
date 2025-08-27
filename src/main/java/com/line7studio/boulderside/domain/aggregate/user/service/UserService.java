package com.line7studio.boulderside.domain.aggregate.user.service;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

public interface UserService {
	User getUserById(Long userId);

	User getUserByPhone(String phoneNumber);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}
