package com.line7studio.boulderside.domain.aggregate.user.service;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

import java.util.List;

public interface UserService {
	User getUserById(Long userId);

	User findByPhone(String phoneNumber);

	boolean isUserIdDuplicate(String email);
  
  List<User> findAllById(List<Long> userIdList);
}
