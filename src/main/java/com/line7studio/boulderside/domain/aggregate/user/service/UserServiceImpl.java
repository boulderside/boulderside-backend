package com.line7studio.boulderside.domain.aggregate.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserSex;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
	public boolean existsByPhone(String phone) {
		return userRepository.existsByPhone(phone);
	}

	@Override
	@Transactional
	public User createUser(String nickname, String profileImageUrl, String phone, UserRole userRole, UserSex userSex,
		Level userLevel, String name, String email, String password) {
		if (userRepository.existsByEmail(email) || userRepository.existsByPhone(phone)) {
			throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
		}

		User user = User.builder()
			.nickname(nickname)
			.profileImageUrl(profileImageUrl)
			.phone(phone)
			.userRole(userRole)
			.userSex(userSex)
			.userLevel(userLevel)
			.name(name)
			.email(email)
			.password(passwordEncoder.encode(password))
			.build();

		User savedUser = userRepository.save(user);
		log.info("[회원가입 완료] userId={}, email={}, nickname={}", savedUser.getId(), savedUser.getEmail(),
			savedUser.getNickname());
		return savedUser;
	}

	@Override
	public List<User> findAllById(List<Long> userIdList) {
		return userRepository.findAllByIdIn(userIdList);
	}

	@Override
	@Transactional(readOnly = false)
	public User updateUserByPhone(String phoneNumber, String email, String password) {
		User user = userRepository.findByPhone(phoneNumber)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		if (user.getEmail() != null && !user.getEmail().isBlank()) {
			throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
		}

		user.updateAccount(email, passwordEncoder.encode(password));
		User savedUser = userRepository.save(user);
		log.info("[계정 연동 완료] userId={}, phone={}, email={}",
			savedUser.getId(), savedUser.getPhone(), savedUser.getEmail());

		return savedUser;
	}

}