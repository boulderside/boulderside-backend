package com.line7studio.boulderside.domain.route;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.domain.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "routes")
public class Route extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 바위 ID (FK) */
	@Column(name = "boulder_id", nullable = false)
	private Long boulderId;

	/** 루트 이름 */
	@Column(name = "name")
	private String name;

	/** 루트 설명 */
	@Column(name = "description")
	private String description;

	/** 난이도 (route_level) */
    @Enumerated(value = EnumType.STRING)
	@Column(name = "route_level")
	private Level routeLevel;

	/** 첫 등반자 이름 (pioneer_name) */
	@Column(name = "pioneer_name")
	private String pioneerName;

	/** 조회 수 */
	@Column(name = "view_count")
	private Long viewCount;

	/** 좋아요 수 */
	@Column(name = "like_count")
	private Long likeCount;

	/** 등반자 수 */
	@Column(name = "climber_count")
	private Long climberCount;

	/** 댓글 수 */
	@Column(name = "comment_count")
	private Long commentCount;

	@Builder(access = AccessLevel.PRIVATE)
	private Route(Long boulderId, String name, String description, Level routeLevel,
	              String pioneerName) {
		this.boulderId = boulderId;
		this.name = name;
		this.description = description;
		this.routeLevel = routeLevel;
		this.pioneerName = pioneerName;
		this.viewCount = 0L;
		this.likeCount = 0L;
		this.climberCount = 0L;
		this.commentCount = 0L;
	}

	/**
	 * 정적 팩토리 메서드 - 루트 생성
	 */
	public static Route create(Long boulderId, String name, String description, Level routeLevel,
	                            String pioneerName) {
		validateBoulderId(boulderId);
		validateName(name);
		validateDescription(description);
		validatePioneerName(pioneerName);

		return Route.builder()
			.boulderId(boulderId)
			.name(name)
			.description(description)
			.routeLevel(routeLevel)
			.pioneerName(pioneerName)
			.build();
	}

	/**
	 * 루트 정보 업데이트 (검증 포함)
	 */
	public void update(Long boulderId, String name, String description, Level routeLevel,
	                   String pioneerName) {
		validateBoulderId(boulderId);
		validateName(name);
		validateDescription(description);
		validatePioneerName(pioneerName);

		this.boulderId = boulderId;
		this.name = name;
		this.description = description;
		this.routeLevel = routeLevel;
		this.pioneerName = pioneerName;
	}

	public void incrementViewCount() {
		this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
	}

	public void incrementLikeCount() {
		this.likeCount = (this.likeCount == null) ? 1 : this.likeCount + 1;
	}

	public void decrementLikeCount() {
		this.likeCount = (this.likeCount == null || this.likeCount <= 0) ? 0 : this.likeCount - 1;
	}

	public void incrementClimberCount() {
		this.climberCount = (this.climberCount == null) ? 1 : this.climberCount + 1;
	}

	public void decrementClimberCount() {
		this.climberCount = (this.climberCount == null || this.climberCount <= 0) ? 0 : this.climberCount - 1;
	}

	public void incrementCommentCount() {
		this.commentCount = (this.commentCount == null) ? 1 : this.commentCount + 1;
	}

	public void decrementCommentCount() {
		this.commentCount = (this.commentCount == null || this.commentCount <= 0) ? 0 : this.commentCount - 1;
	}

	// === Private 검증 메서드들 ===

	private static void validateBoulderId(Long boulderId) {
		if (boulderId == null) {
			throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "바위 ID는 필수입니다.");
		}
	}

	private static void validateName(String name) {
		if (name != null && name.length() > 100) {
			throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "루트 이름은 100자를 초과할 수 없습니다.");
		}
	}

	private static void validateDescription(String description) {
		if (description != null && description.length() > 1000) {
			throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "루트 설명은 1000자를 초과할 수 없습니다.");
		}
	}

	private static void validatePioneerName(String pioneerName) {
		if (pioneerName != null && pioneerName.length() > 100) {
			throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "첫 등반자 이름은 100자를 초과할 수 없습니다.");
		}
	}
}
