package com.line7studio.boulderside.domain.user;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import com.line7studio.boulderside.domain.user.enums.UserRole;
import com.line7studio.boulderside.domain.user.enums.UserSex;
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
	@Column(name = "nickname")
	private String nickname;

	/** OAuth 제공자 정보 */
	@Enumerated(EnumType.STRING)
	@Column(name = "provider_type")
	private AuthProviderType providerType;

	@Column(name = "provider_user_id")
	private String providerUserId;

	@Column(name = "provider_email")
	private String providerEmail;

	/** 사용자 역할 (예: ADMIN, USER 등) */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	@Builder.Default
	private UserRole userRole = UserRole.ROLE_USER;

	/** 사용자 프로필 이미지 */
	@Column(name = "profile_image_url")
	private String profileImageUrl;

	/** 휴대폰 번호 (선택 입력) */
	@Column(name = "phone")
	private String phone;

	/** 성별 */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_sex")
	private UserSex userSex;

	/** 사용자 레벨 (공통 Level enum 사용) */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_level")
	@Builder.Default
	private Level userLevel = Level.V0;

	/** 실제 이름 */
	@Column(name = "name")
	private String name;

	/** 이메일 */
	@Column(name = "email")
	private String email;

	/** Refresh Token */
	@Column(name = "refresh_token")
	private String refreshToken;

	/** FCM Token */
	@Column(name = "fcm_token", length = 512)
	private String fcmToken;

	/** 사용자 상태 (예: ACTIVE, INACTIVE, BANNED 등) */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_status")
	@Builder.Default
	private UserStatus userStatus = UserStatus.ACTIVE;

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
}
