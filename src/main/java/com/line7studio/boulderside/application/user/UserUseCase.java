package com.line7studio.boulderside.application.user;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.application.user.dto.response.UserInfoResponse;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.service.UserService;

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
