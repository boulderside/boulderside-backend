package com.line7studio.boulderside.domain.aggregate.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public User findByPhone(String phoneNumber) {
		return userRepository.findByPhone(phoneNumber).orElse(null);
	}

	@Override
	public boolean isUserIdDuplicate(String email) {
		return userRepository.existsByEmail(email);
	}
}