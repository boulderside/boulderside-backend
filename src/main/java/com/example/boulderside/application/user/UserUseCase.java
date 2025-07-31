package com.example.boulderside.application.user;

import org.springframework.stereotype.Service;

import com.example.boulderside.application.user.dto.response.UserInfoResponse;
import com.example.boulderside.domain.aggregate.user.entity.User;
import com.example.boulderside.domain.aggregate.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserUseCase {
	private final UserService userService;

	public UserInfoResponse getUserInfo(Long id) {
		User user = userService.getUserById(id);
		return UserInfoResponse.builder()
			.name(user.getName())
			.build();
	}
}
