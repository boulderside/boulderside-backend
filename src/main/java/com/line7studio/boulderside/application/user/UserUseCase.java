package com.line7studio.boulderside.application.user;

import com.line7studio.boulderside.application.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.application.user.dto.response.UserInfoResponse;
import com.line7studio.boulderside.common.security.provider.AESProvider;
import com.line7studio.boulderside.controller.user.request.PhoneLinkRequest;
import com.line7studio.boulderside.controller.user.request.SignupRequest;
import com.line7studio.boulderside.controller.user.response.PhoneLookupResponse;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.provider.PhoneAuthProvider;
import com.line7studio.boulderside.domain.aggregate.user.service.UserService;
import com.line7studio.boulderside.infrastructure.redis.RedisKeyPrefixType;
import com.line7studio.boulderside.infrastructure.redis.RedisProvider;
import com.line7studio.boulderside.infrastructure.s3.S3FolderType;
import com.line7studio.boulderside.infrastructure.s3.S3Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;
import java.util.concurrent.TimeUnit;

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
	public UserInfoResponse getUserInfo(Long id) {
		User user = userService.getUserById(id);
		return UserInfoResponse.builder()
			.name(user.getName())
			.build();
	}

    @Transactional(readOnly = true)
	public boolean isUserIdDuplicate(String email) {
		return userService.existsByEmail(email);
	}

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
	public void sendAuthCode(String phoneNumber) {
        // 인증번호 생성
        String verificationCode = generateAuthCode();
		String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);

        // User 존재 여부 검증
        userService.validateUserNotExistsByPhone(encodedPhoneNumber);

        // Redis 저장
		String redisKey = RedisKeyPrefixType.PHONE_AUTH.of(encodedPhoneNumber);
        redisProvider.set(redisKey, verificationCode, 3, TimeUnit.MINUTES);
		phoneAuthProvider.sendCertificationCode(phoneNumber, verificationCode);
	}

    @Transactional(readOnly = true)
	public PhoneLookupResponse lookupUserByPhone(String phoneNumber) {
		String encodedPhoneNumber = aesProvider.encrypt(phoneNumber);
        User user = userService.getUserByPhone(encodedPhoneNumber);
        return PhoneLookupResponse.from(user);
	}

    @Transactional
	public void linkAccountByPhone(PhoneLinkRequest request) {
		String encodedPhone = aesProvider.encrypt(request.phoneNumber());
		userService.updateUserByPhone(encodedPhone, request.email(), request.password());
	}

    @Transactional
	public void signUp(SignupRequest request, MultipartFile file) {
		String encodedPhone = aesProvider.encrypt(request.phone());

        CreateUserCommand createUserCommand = new CreateUserCommand(
                request.nickname(),
                encodedPhone,
                request.userRole(),
                request.userSex(),
                request.userLevel(),
                request.name(),
                request.email(),
                request.password()
        );

        // 3) 유저 저장
        User savedUser = userService.createUser(createUserCommand);

        String profileImageUrl = s3Provider.imageUpload(file, S3FolderType.PROFILE).url();
        userService.updateUserProfileImage(savedUser.getId(), profileImageUrl);
    }

	private String generateAuthCode() {
		int code = 100000 + random.nextInt(900000);
		return String.valueOf(code);
	}
}
