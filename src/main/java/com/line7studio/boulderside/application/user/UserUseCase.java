package com.line7studio.boulderside.application.user;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.application.user.dto.response.UserInfoResponse;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ExternalApiException;
import com.line7studio.boulderside.common.security.provider.AESProvider;
import com.line7studio.boulderside.controller.user.response.LinkAccountResponse;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.provider.PhoneAuthProvider;
import com.line7studio.boulderside.domain.aggregate.user.service.UserService;
import com.line7studio.boulderside.infrastructure.redis.RedisKeyPrefixType;
import com.line7studio.boulderside.infrastructure.redis.RedisProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserUseCase {
	private final UserService userService;
	private final AESProvider aesProvider;
	private final PhoneAuthProvider phoneAuthProvider;
	private final RedisProvider redisProvider;

	public UserInfoResponse getUserInfo(Long id) {
		User user = userService.getUserById(id);
		return UserInfoResponse.builder()
			.name(user.getName())
			.build();
	}

	public boolean isUserIdDuplicate(String email) {
		return userService.existsByEmail(email);
	}

	public boolean verifyAuthCode(String phoneNumber, String code) {
		String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);
		String redisKey = RedisKeyPrefixType.PHONE_AUTH.of(encodedPhoneNumber);

		String storedCode = redisProvider.get(redisKey, String.class).orElse(null);

		if (storedCode == null || !storedCode.equals(code)) {
			return false;
		}

		redisProvider.delete(redisKey);
		return true;
	}

	public void sendAuthCode(String phoneNumber) {
		String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);

		// 휴대폰 번호로 유저 존재 여부 확인
		if (userService.existsByPhone(encodedPhoneNumber)) {
			User existingUser = userService.getUserByPhone(encodedPhoneNumber);

			if (existingUser.getEmail() != null && !existingUser.getEmail().isBlank()) {
				throw new ExternalApiException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
			}
		}

		String verificationCode = generateAuthCode();
		String redisKey = RedisKeyPrefixType.PHONE_AUTH.of(encodedPhoneNumber);
		try {
			redisProvider.set(redisKey, verificationCode, 3, TimeUnit.MINUTES);
		} catch (Exception e) {
			log.error("Redis 저장 실패 - key={}, value={}, reason={}", redisKey, verificationCode, e.getMessage(), e);
			throw new ExternalApiException(ErrorCode.REDIS_STORE_FAILED);
		}

		phoneAuthProvider.sendCertificationCode(phoneNumber, verificationCode);
	}

	public LinkAccountResponse linkAccountWithPhone(String phoneNumber) {
		String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);
		User user = userService.getUserByPhone(encodedPhoneNumber);

		return LinkAccountResponse.from(user);
	}

	private String generateAuthCode() {
		Random random = new Random();
		int code = 100000 + random.nextInt(900000);
		return String.valueOf(code);
	}
}