package com.line7studio.boulderside.domain.feature.user.service;

import com.line7studio.boulderside.usecase.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.user.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import com.line7studio.boulderside.domain.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	public User createUser(CreateUserCommand createUserCommand) {
		validateDuplicateNickname(createUserCommand.nickname());

		User user = User.builder()
			.nickname(createUserCommand.nickname())
			.userRole(createUserCommand.userRole())
			.build();

		return userRepository.save(user);
	}

	public List<User> findAllById(List<Long> userIdList) {
		return userRepository.findAllByIdIn(userIdList);
	}

	public void updateUserProfileImage(Long userId, String profileImageUrl) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		user.updateProfileImage(profileImageUrl);
		User savedUser = userRepository.save(user);
		log.info("[프로필 이미지 업데이트 완료] userId={}, profileImageUrl={}",
			savedUser.getId(), savedUser.getProfileImageUrl());
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public boolean isNicknameAvailable(String nickname) {
		return !userRepository.existsByNickname(nickname);
	}

	public void updateNickname(Long userId, String nickname) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		if (Objects.equals(user.getNickname(), nickname)) {
			return;
		}

		validateDuplicateNickname(nickname);
		user.updateNickname(nickname);
		userRepository.save(user);
	}

	public User updateUserRole(Long userId, UserRole userRole) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		user.updateRole(userRole);
		return userRepository.save(user);
	}

	private void validateDuplicateNickname(String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
		}
	}

}
