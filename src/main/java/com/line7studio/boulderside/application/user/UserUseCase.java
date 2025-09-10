package com.line7studio.boulderside.application.user;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.line7studio.boulderside.application.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.common.security.provider.AESProvider;
import com.line7studio.boulderside.controller.user.request.PhoneLinkRequest;
import com.line7studio.boulderside.controller.user.request.SignupRequest;
import com.line7studio.boulderside.controller.user.response.FindIdByPhoneResponse;
import com.line7studio.boulderside.controller.user.response.PhoneLookupResponse;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.provider.PhoneAuthProvider;
import com.line7studio.boulderside.domain.aggregate.user.service.UserService;
import com.line7studio.boulderside.infrastructure.redis.RedisKeyPrefixType;
import com.line7studio.boulderside.infrastructure.redis.RedisProvider;
import com.line7studio.boulderside.infrastructure.s3.S3FolderType;
import com.line7studio.boulderside.infrastructure.s3.S3ObjectInfo;
import com.line7studio.boulderside.infrastructure.s3.S3Provider;

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
	private final S3Provider s3Provider;
	private final Random random = new Random();

	@Transactional(readOnly = true)
	public boolean isUserIdDuplicate(String email) {
		return userService.existsByEmail(email);
	}

	@Transactional(readOnly = true)
	public boolean verifyAuthCode(String phoneNumber, String code) {
		//String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);
		String redisKey = RedisKeyPrefixType.PHONE_AUTH.of(phoneNumber);

		String storedCode = redisProvider.get(redisKey, String.class).orElse(null);

		if (storedCode == null || !storedCode.equals(code)) {
			return false;
		}

		redisProvider.delete(redisKey);
		return true;
	}

	@Transactional(readOnly = true)
	public void sendAuthCode(String phoneNumber) {
		// 인증번호 생성
		String verificationCode = generateAuthCode();

		//String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);

		// User 존재 여부 검증
		// userService.validateUserNotExistsByPhone(phoneNumber);

		// Redis 저장
		String redisKey = RedisKeyPrefixType.PHONE_AUTH.of(phoneNumber);
		redisProvider.set(redisKey, verificationCode, 3, TimeUnit.MINUTES);
		phoneAuthProvider.sendCertificationCode(phoneNumber, verificationCode);
	}

	@Transactional(readOnly = true)
	public PhoneLookupResponse lookupUserByPhone(String phoneNumber) {
		//String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);

		User user = userService.getUserByPhone(phoneNumber);
		return PhoneLookupResponse.from(user);
	}

	@Transactional
	public void linkAccountByPhone(PhoneLinkRequest request) {
		//String encodedPhone = aesProvider.encrypt(request.phoneNumber());

		userService.updateUserByPhone(request.phoneNumber(), request.email(), request.password());
	}

	@Transactional
	public void signUp(SignupRequest request, MultipartFile file) {
		//String encodedPhone = aesProvider.encrypt(request.phone());

		CreateUserCommand createUserCommand = new CreateUserCommand(
			request.nickname(),
			request.phoneNumber(),
			request.userRole(),
			request.userSex(),
			request.userLevel(),
			request.name(),
			request.email(),
			request.password()
		);

		// 3) 유저 저장
		User savedUser = userService.createUser(createUserCommand);

		S3ObjectInfo s3ObjectInfo = s3Provider.imageUpload(file, S3FolderType.PROFILE);
		if (s3ObjectInfo != null && s3ObjectInfo.url() != null) {
			userService.updateUserProfileImage(savedUser.getId(), s3ObjectInfo.url());
		}
	}

	@Transactional(readOnly = true)
	public FindIdByPhoneResponse findIdByPhone(String phoneNumber) {
		// String encodedPhoneNumber = passwordEncoder.encode(phoneNumber);

		User user = userService.findUserByPhone(phoneNumber);
		return FindIdByPhoneResponse.from(user);
	}

	@Transactional
	public void changePassword(String phoneNumber, String newPassword) {
		// String encodedPhoneNumber = passwordEncoder.encode(phoneNumber);

		userService.updatePasswordByPhone(phoneNumber, newPassword);
	}

	private String generateAuthCode() {
		int code = 100000 + random.nextInt(900000);
		return String.valueOf(code);
	}

}
