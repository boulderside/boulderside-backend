package com.line7studio.boulderside.domain.user;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import com.line7studio.boulderside.domain.user.enums.ConsentType;
import com.line7studio.boulderside.domain.user.enums.UserRole;
import com.line7studio.boulderside.domain.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
	name = "users",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_user_provider", columnNames = {"provider_type", "provider_user_id"})
	}
)
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/** 사용자 닉네임 */
	@Column(name = "nickname", length = 20)
	private String nickname;

	/** OAuth 제공자 정보 */
	@Enumerated(EnumType.STRING)
	@Column(name = "provider_type", length = 20)
	private AuthProviderType providerType;

	@Column(name = "provider_user_id", length = 255)
	private String providerUserId;

	@Column(name = "provider_email", length = 255)
	private String providerEmail;

	/** 사용자 프로필 이미지 */
	@Column(name = "profile_image_url", length = 2000)
	private String profileImageUrl;

	/** 사용자 레벨 (공통 Level enum 사용) */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_level", nullable = false, length = 10)
	@Builder.Default
	private Level userLevel = Level.V0;

	/** Refresh Token */
	@Column(name = "refresh_token", length = 512)
	private String refreshToken;

	/** FCM Token */
	@Column(name = "fcm_token", length = 512)
	private String fcmToken;

    /** 사용자 역할 (예: ADMIN, USER 등) */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 20)
    @Builder.Default
    private UserRole userRole = UserRole.ROLE_USER;

	/** 사용자 상태 (예: ACTIVE, INACTIVE, BANNED 등) */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_status", nullable = false, length = 20)
	@Builder.Default
	private UserStatus userStatus = UserStatus.ACTIVE;

	/** 푸시 알림 ON/OFF */
	@Column(name = "push_enabled", nullable = false)
	@Builder.Default
	private Boolean pushEnabled = false;

	/** 마케팅 수신 동의 */
	@Column(name = "marketing_agreed", nullable = false)
	@Builder.Default
	private Boolean marketingAgreed = false;

	/** 개인정보 수집 및 활용 동의 */
	@Column(name = "privacy_agreed", nullable = false)
	@Builder.Default
	private Boolean privacyAgreed = false;

	/** 서비스 이용 약관 동의 */
	@Column(name = "service_terms_agreed", nullable = false)
	@Builder.Default
	private Boolean serviceTermsAgreed = false;

	/** 14세 이상 동의 */
	@Column(name = "over_fourteen_agreed", nullable = false)
	@Builder.Default
	private Boolean overFourteenAgreed = false;

	public void updateRole(UserRole role) {
		this.userRole = role;
	}

	public void updateProfileImage(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public void updateConsent(ConsentType type, boolean agreed) {
		switch (type) {
			case PUSH -> this.pushEnabled = agreed;
			case MARKETING -> this.marketingAgreed = agreed;
			case PRIVACY -> this.privacyAgreed = agreed;
			case SERVICE_TERMS -> this.serviceTermsAgreed = agreed;
			case OVER_FOURTEEN -> this.overFourteenAgreed = agreed;
		}
	}
}
