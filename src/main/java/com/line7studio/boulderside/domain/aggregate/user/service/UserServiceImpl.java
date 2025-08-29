package com.line7studio.boulderside.domain.aggregate.user.service;

import com.line7studio.boulderside.application.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ExternalApiException;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	@Override
	public User getUserByPhone(String phoneNumber) {
		return userRepository.findByPhone(phoneNumber)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

    @Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User createUser(CreateUserCommand createUserCommand) {
		if (existsByEmail(createUserCommand.email()) &&
                userRepository.existsByPhone(createUserCommand.encodedPhone())) {
			throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
		}

        User user = User.builder()
                .nickname(createUserCommand.nickname())
                .phone(createUserCommand.encodedPhone())
                .userRole(createUserCommand.userRole())
                .userSex(createUserCommand.userSex())
                .userLevel(createUserCommand.userLevel())
                .name(createUserCommand.name())
                .email(createUserCommand.email())
                .password(passwordEncoder.encode(createUserCommand.rawPassword()))
                .build();

        return userRepository.save(user);
	}

	@Override
	public List<User> findAllById(List<Long> userIdList) {
		return userRepository.findAllByIdIn(userIdList);
	}

	@Override
	public void updateUserByPhone(String phoneNumber, String email, String password) {
		User user = userRepository.findByPhone(phoneNumber)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		if (user.getEmail() != null && !user.getEmail().isBlank()) {
			throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
		}

		user.updateAccount(email, passwordEncoder.encode(password));
		User savedUser = userRepository.save(user);
		log.info("[계정 연동 완료] userId={}, phone={}, email={}",
			savedUser.getId(), savedUser.getPhone(), savedUser.getEmail());

    }

	@Override
	public void updateUserProfileImage(Long userId, String profileImageUrl) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		user.updateProfileImage(profileImageUrl);
		User savedUser = userRepository.save(user);
		log.info("[프로필 이미지 업데이트 완료] userId={}, profileImageUrl={}", 
			savedUser.getId(), savedUser.getProfileImageUrl());
    }

    @Override
    public void validateUserNotExistsByPhone(String encodedPhoneNumber) {
        User user = getUserByPhone(encodedPhoneNumber);
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            throw new ExternalApiException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
    }
}
