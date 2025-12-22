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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
	name = "route_instagrams",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_route_instagram", columnNames = {"route_id", "instagram_id"})
	}
)
public class RouteInstagram extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 루트 ID (FK) */
	@Column(name = "route_id", nullable = false)
	private Long routeId;

	/** 연관 Instagram ID (FK) */
	@Column(name = "instagram_id", nullable = false)
	private Long instagramId;

	@Builder(access = AccessLevel.PRIVATE)
	private RouteInstagram(Long routeId, Long instagramId) {
		this.routeId = routeId;
		this.instagramId = instagramId;
	}

	/**
	 * 정적 팩토리 메서드 - RouteInstagram 생성
	 */
	public static RouteInstagram create(Long routeId, Long instagramId) {
		validateRouteId(routeId);
		validateInstagramId(instagramId);

		return RouteInstagram.builder()
			.routeId(routeId)
			.instagramId(instagramId)
			.build();
	}

	// === Private 검증 메서드들 ===

	private static void validateRouteId(Long routeId) {
		if (routeId == null) {
			throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "루트 ID는 필수입니다.");
		}
	}

	private static void validateInstagramId(Long instagramId) {
		if (instagramId == null) {
			throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "Instagram ID는 필수입니다.");
		}
	}
}