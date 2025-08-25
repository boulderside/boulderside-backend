package com.line7studio.boulderside.domain.aggregate.user.service;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

import java.util.List;

public interface UserService {
	User getUserById(Long userId);

    List<User> findAllById(List<Long> userIdList);
}
