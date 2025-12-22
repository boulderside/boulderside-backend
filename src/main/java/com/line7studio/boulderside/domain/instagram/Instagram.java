package com.line7studio.boulderside.domain.instagram;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "instagrams")
public class Instagram extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    /** 등록한 사용자 ID (FK) */
    @Column(name = "user_id", nullable = false)
    private Long userId;

	/** Instagram 게시물 URL */
	@Column(name = "url", nullable = false, length = 500)
	private String url;

	@Builder(access = AccessLevel.PRIVATE)
	private Instagram(String url, Long userId) {
		this.url = url;
		this.userId = userId;
	}

	/**
	 * 정적 팩토리 메서드 - Instagram 생성
	 */
	public static Instagram create(String url, Long userId) {
		validateUrl(url);
		validateUserId(userId);

		return Instagram.builder()
			.url(url)
			.userId(userId)
			.build();
	}

	/**
	 * Instagram 정보 업데이트 (검증 포함)
	 */
	public void update(String url, Long userId) {
		validateUrl(url);
		validateUserId(userId);

		this.url = url;
		this.userId = userId;
	}

	// === Private 검증 메서드들 ===

	private static void validateUrl(String url) {
		if (url == null || url.isBlank()) {
			throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "Instagram URL은 필수입니다.");
		}
		if (url.length() > 500) {
			throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "Instagram URL은 500자를 초과할 수 없습니다.");
		}
		// Instagram URL 형식 검증 (선택적)
		if (!url.contains("instagram.com")) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED, "올바른 Instagram URL 형식이 아닙니다.");
		}
	}

	private static void validateUserId(Long userId) {
		if (userId == null) {
			throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "사용자 ID는 필수입니다.");
		}
	}
}