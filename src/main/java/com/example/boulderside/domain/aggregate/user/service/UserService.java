package com.example.boulderside.domain.aggregate.user.service;

import com.example.boulderside.domain.aggregate.user.entity.User;

public interface UserService {
	User getUserById(Long userId);
}
