package com.line7studio.boulderside.domain.user.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.board.service.BoardPostService;
import com.line7studio.boulderside.domain.comment.service.CommentService;
import com.line7studio.boulderside.domain.enums.PostStatus;
import com.line7studio.boulderside.domain.mate.service.MatePostService;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.UserConsentHistory;
import com.line7studio.boulderside.domain.user.UserLoginHistory;
import com.line7studio.boulderside.domain.user.UserMeta;
import com.line7studio.boulderside.domain.user.UserStatusHistory;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import com.line7studio.boulderside.domain.user.enums.ConsentType;
import com.line7studio.boulderside.domain.user.enums.UserRole;
import com.line7studio.boulderside.domain.user.enums.UserStatus;
import com.line7studio.boulderside.domain.user.enums.UserStatusChangeReason;
import com.line7studio.boulderside.domain.user.repository.UserConsentHistoryRepository;
import com.line7studio.boulderside.domain.user.repository.UserLoginHistoryRepository;
import com.line7studio.boulderside.domain.user.repository.UserMetaRepository;
import com.line7studio.boulderside.domain.user.repository.UserRepository;
import com.line7studio.boulderside.domain.user.repository.UserStatusHistoryRepository;
import com.line7studio.boulderside.usecase.user.dto.response.CreateUserCommand;
import com.line7studio.boulderside.controller.user.request.UpdateConsentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final UserMetaRepository userMetaRepository;
	private final UserConsentHistoryRepository userConsentHistoryRepository;
	private final UserStatusHistoryRepository userStatusHistoryRepository;
	private final UserLoginHistoryRepository userLoginHistoryRepository;
	private final BoardPostService boardPostService;
	private final MatePostService matePostService;
	private final CommentService commentService;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	public Optional<User> findByProvider(AuthProviderType providerType, String providerUserId) {
		return userRepository.findByProviderTypeAndProviderUserId(providerType, providerUserId);
	}

	@Transactional
	public UserMeta updateConsent(Long userId, UpdateConsentRequest request) {
		UserMeta userMeta = userMetaRepository.findByUserId(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		userMeta.updateConsent(request.consentType(), request.agreed());
		saveConsentHistory(userId, request.consentType(), request.agreed());
		return userMeta;
	}

	public UserMeta getUserMeta(Long userId) {
		return userMetaRepository.findByUserId(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<UserConsentHistory> getUserConsentHistories(Long userId) {
		getUserById(userId);
		return userConsentHistoryRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
	}

	@Transactional(readOnly = true)
	public List<UserLoginHistory> getUserLoginHistories(Long userId) {
		getUserById(userId);
		return userLoginHistoryRepository.findAllByUserIdOrderByLoginAtDesc(userId);
	}

	@Transactional(readOnly = true)
	public List<UserStatusHistory> getUserStatusHistories(Long userId) {
		getUserById(userId);
		return userStatusHistoryRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
	}

	@Transactional
	public User createUser(CreateUserCommand command) {
		validateDuplicateNickname(command.nickname());

		// 1. User 생성
		User user = User.builder()
			.nickname(command.nickname())
			.providerType(command.providerType())
			.providerUserId(command.providerUserId())
			.providerEmail(command.providerEmail())
			.userRole(command.userRole())
			.userStatus(UserStatus.ACTIVE)
			.build();
		User savedUser = userRepository.save(user);

		// 2. UserMeta 생성
		LocalDateTime now = LocalDateTime.now();
		UserMeta userMeta = UserMeta.builder()
			.userId(savedUser.getId())
			.pushEnabled(true) // 기본값 ON
			.privacyAgreed(command.privacyAgreed())
			.privacyAgreedCreatedAt(now)
			.serviceTermsAgreed(command.serviceTermsAgreed())
			.serviceTermsAgreedCreatedAt(now)
			.overFourteenAgreed(command.overFourteenAgreed())
			.overFourteenAgreedCreatedAt(now)
			.marketingAgreed(command.marketingAgreed())
			.marketingAgreedCreatedAt(now)
			.marketingAgreedUpdatedAt(now)
			.build();
		userMetaRepository.save(userMeta);

		// 3. 약관 동의 이력 저장 (ConsentHistory)
		saveConsentHistory(savedUser.getId(), ConsentType.PRIVACY, command.privacyAgreed());
		saveConsentHistory(savedUser.getId(), ConsentType.SERVICE_TERMS, command.serviceTermsAgreed());
		saveConsentHistory(savedUser.getId(), ConsentType.OVER_FOURTEEN, command.overFourteenAgreed());
		saveConsentHistory(savedUser.getId(), ConsentType.MARKETING, command.marketingAgreed());

		// 4. 상태 변경 이력 저장 (초기 가입 ACTIVE)
		saveStatusHistory(savedUser.getId(), null, UserStatus.ACTIVE, UserStatusChangeReason.SYSTEM_AUTO, "회원가입", null);

		return savedUser;
	}

	@Transactional
	public void withdrawUser(Long userId, String reason) {
		User user = getUserById(userId);
		UserStatus previousStatus = user.getUserStatus();

		if (previousStatus == UserStatus.INACTIVE || previousStatus == UserStatus.BANNED) {
			throw new BusinessException(ErrorCode.USER_NOT_FOUND, "이미 탈퇴하거나 정지된 계정입니다.");
		}

		// 상태 변경
		user.updateStatus(UserStatus.INACTIVE);
		userRepository.save(user);

		// 사용자 콘텐츠 상태 삭제 처리
		boardPostService.updateBoardPostsStatusByUser(userId, PostStatus.DELETED);
		matePostService.updateMatePostsStatusByUser(userId, PostStatus.DELETED);
		commentService.updateCommentsStatusByUser(userId, PostStatus.DELETED);

		// 이력 저장
		saveStatusHistory(userId, previousStatus, UserStatus.INACTIVE, UserStatusChangeReason.USER_REQUEST, reason, userId);
	}

	public List<User> findAllById(List<Long> userIdList) {
		return userRepository.findAllByIdIn(userIdList);
	}

	@Transactional
	public void updateUserProfileImage(Long userId, String profileImageUrl) {
		User user = getUserById(userId);
		user.updateProfileImage(profileImageUrl);
		// save는 Transactional 안에서는 dirty checking으로 생략 가능하나 명시적으로 둠
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<String> getAllFcmTokens() {
		return userRepository.findAllFcmTokens();
	}

	@Transactional(readOnly = true)
	public Optional<String> getFcmTokenForPush(Long userId) {
		return userRepository.findFcmTokenByUserIdAndPushEnabled(userId);
	}

	public boolean isNicknameAvailable(String nickname) {
		return !userRepository.existsByNickname(nickname);
	}

	@Transactional
	public void updateNickname(Long userId, String nickname) {
		User user = getUserById(userId);

		if (Objects.equals(user.getNickname(), nickname)) {
			return;
		}
		validateDuplicateNickname(nickname);
		user.updateNickname(nickname);
	}

	@Transactional
	public User updateUserRole(Long userId, UserRole userRole) {
		User user = getUserById(userId);
		user.updateRole(userRole);
		return user;
	}

	@Transactional
	public User updateUserStatus(Long userId, UserStatus newStatus, UserStatusChangeReason reasonType, String reasonDetail, Long changedBy) {
		if (newStatus == null) {
			throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD, "변경할 상태가 필요합니다.");
		}
		User user = getUserById(userId);
		UserStatus previousStatus = user.getUserStatus();

		if (previousStatus == newStatus) {
			return user;
		}

		user.updateStatus(newStatus);
		userRepository.save(user);
		if (newStatus == UserStatus.BANNED) {
			boardPostService.updateBoardPostsStatusByUser(userId, PostStatus.BLOCKED);
			matePostService.updateMatePostsStatusByUser(userId, PostStatus.BLOCKED);
			commentService.updateCommentsStatusByUser(userId, PostStatus.BLOCKED);
		} else if (previousStatus == UserStatus.BANNED && newStatus == UserStatus.ACTIVE) {
			boardPostService.restoreBoardPostsStatusByUser(userId);
			matePostService.restoreMatePostsStatusByUser(userId);
			commentService.restoreCommentsStatusByUser(userId);
		}
		saveStatusHistory(
			userId,
			previousStatus,
			newStatus,
			reasonType == null ? UserStatusChangeReason.ADMIN_ACTION : reasonType,
			reasonDetail,
			changedBy
		);

		return user;
	}

	@Transactional
	public void updateRefreshToken(Long userId, String refreshToken) {
		User user = getUserById(userId);
		user.updateRefreshToken(refreshToken);
	}

	@Transactional
	public void updateFcmToken(Long userId, String fcmToken) {
		User user = getUserById(userId);
		user.updateFcmToken(fcmToken);
	}

	@Transactional
	public void logout(Long userId) {
		User user = getUserById(userId);
		user.updateRefreshToken(null);
		user.updateFcmToken(null);
	}

	public boolean isWithdrawnWithinDays(Long userId, long days) {
		return userStatusHistoryRepository
			.findTopByUserIdAndNewStatusOrderByCreatedAtDesc(userId, UserStatus.INACTIVE)
			.filter(history -> history.getChangeReasonType() == UserStatusChangeReason.USER_REQUEST)
			.map(history -> history.getCreatedAt() != null
				&& history.getCreatedAt().isAfter(LocalDateTime.now().minusDays(days)))
			.orElse(false);
	}

	private void validateDuplicateNickname(String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
		}
	}

	private void saveConsentHistory(Long userId, ConsentType type, boolean agreed) {
		UserConsentHistory history = UserConsentHistory.builder()
			.userId(userId)
			.consentType(type)
			.agreed(agreed)
			.consentVersion("1.0") // TODO: 버전 관리 정책 필요
			.build();
		userConsentHistoryRepository.save(history);
	}

	private void saveStatusHistory(Long userId, UserStatus prev, UserStatus next,
								   UserStatusChangeReason reasonType, String detail, Long changedBy) {
		UserStatusHistory history = UserStatusHistory.builder()
			.userId(userId)
			.previousStatus(prev != null ? prev : UserStatus.PENDING) // 가입 시 prev는 null일 수 있음
			.newStatus(next)
			.changeReasonType(reasonType)
			.changeReasonDetail(detail)
			.changedBy(changedBy)
			.build();
		userStatusHistoryRepository.save(history);
	}
}
