package com.line7studio.boulderside.domain.aggregate.boulder.entity;

import com.line7studio.boulderside.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	@Default
	private Long likeCount = 0L;

	/** 조회 수 */
	@Column(name = "view_count")
	@Default
	private Long viewCount = 0L;

	public void update(Long regionId, Long sectorId, String name, String description, Double latitude, Double longitude) {
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
}
