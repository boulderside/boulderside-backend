package com.line7studio.boulderside.domain.feature.boulder.entity;

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
@Table(name = "boulders")
public class Boulder extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 지역 ID (FK) */
	@Column(name = "region_id", nullable = false)
	private Long regionId;

	/** 연관 섹터 ID (FK) */
	@Column(name = "sector_id", nullable = false)
	private Long sectorId;

	/** 바위 이름 */
	@Column(name = "name")
	private String name;

	/** 바위 설명 */
	@Column(name = "description")
	private String description;

	/** 바위 위치 위도 */
	@Column(name = "latitude")
	private Double latitude;

	/** 바위 위치 경도 */
	@Column(name = "longitude")
	private Double longitude;

	/** 좋아요 수 */
	@Column(name = "like_count")
	private Long likeCount;

	/** 조회 수 */
	@Column(name = "view_count")
	private Long viewCount;

	@Builder(access = AccessLevel.PRIVATE)
	private Boulder(Long regionId, Long sectorId, String name, String description, Double latitude, Double longitude) {
		this.regionId = regionId;
		this.sectorId = sectorId;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.likeCount = 0L;
		this.viewCount = 0L;
	}

	/**
	 * 정적 팩토리 메서드 - 바위 생성
	 */
	public static Boulder create(Long regionId, Long sectorId, String name, String description, Double latitude, Double longitude) {
		validateRegionId(regionId);
		validateSectorId(sectorId);
		validateName(name);
		validateDescription(description);
		validateCoordinates(latitude, longitude);

		return Boulder.builder()
			.regionId(regionId)
			.sectorId(sectorId)
			.name(name)
			.description(description)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}

	/**
	 * 바위 정보 업데이트 (검증 포함)
	 */
	public void update(Long regionId, Long sectorId, String name, String description, Double latitude, Double longitude) {
		validateRegionId(regionId);
		validateSectorId(sectorId);
		validateName(name);
		validateDescription(description);
		validateCoordinates(latitude, longitude);

		this.regionId = regionId;
		this.sectorId = sectorId;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
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

	// === Private 검증 메서드들 ===

	private static void validateRegionId(Long regionId) {
		if (regionId == null) {
			throw new IllegalArgumentException("지역 ID는 필수입니다.");
		}
	}

	private static void validateSectorId(Long sectorId) {
		if (sectorId == null) {
			throw new IllegalArgumentException("섹터 ID는 필수입니다.");
		}
	}

	private static void validateName(String name) {
		if (name != null && name.length() > 100) {
			throw new IllegalArgumentException("바위 이름은 100자를 초과할 수 없습니다.");
		}
	}

	private static void validateDescription(String description) {
		if (description != null && description.length() > 1000) {
			throw new IllegalArgumentException("바위 설명은 1000자를 초과할 수 없습니다.");
		}
	}

	private static void validateCoordinates(Double latitude, Double longitude) {
		if (latitude != null && (latitude < -90.0 || latitude > 90.0)) {
			throw new IllegalArgumentException("위도는 -90.0 ~ 90.0 범위여야 합니다.");
		}
		if (longitude != null && (longitude < -180.0 || longitude > 180.0)) {
			throw new IllegalArgumentException("경도는 -180.0 ~ 180.0 범위여야 합니다.");
		}
	}
}