package com.line7studio.boulderside.domain.user;

import java.time.LocalDateTime;

import com.line7studio.boulderside.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_meta")
public class UserMeta extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 사용자 FK */
	@Column(name = "user_id", nullable = false)
	private Long userId;

	/** 푸시 알림 ON/OFF */
	@Column(name = "push_enabled", nullable = false)
	private Boolean pushEnabled;

	/** 마케팅 수신 동의 */
	@Column(name = "marketing_agreed", nullable = false)
	private Boolean marketingAgreed;

	/** 마케팅 수신 동의 생성 시각 */
	@Column(name = "marketing_agreed_created_at")
	private LocalDateTime marketingAgreedCreatedAt;

	/** 개인정보 수집 및 활용 동의 */
	@Column(name = "privacy_agreed", nullable = false)
	private Boolean privacyAgreed;

	/** 개인정보 수집 및 활용 동의 생성 시각 */
	@Column(name = "privacy_agreed_created_at")
	private LocalDateTime privacyAgreedCreatedAt;
}
