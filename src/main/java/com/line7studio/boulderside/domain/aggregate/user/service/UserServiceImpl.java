package com.line7studio.boulderside.domain.aggregate.user.service;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Override
	public User getUserById(Long userId) {
		return null;
	}
}
