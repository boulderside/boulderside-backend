package com.line7studio.boulderside.domain.aggregate.route;

import com.line7studio.boulderside.common.enums.Level;

import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "routes")
public class Route extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 바위 ID (FK) */
	@Column(name = "boulder_id", nullable = false)
	private Long boulderId;

	/** 연관 지역 ID (FK) */
	@Column(name = "region_id", nullable = false)
	private Long regionId;

	/** 연관 섹터 ID (FK) */
	@Column(name = "sector_id", nullable = false)
	private Long sectorId;

	/** 루트 이름 */
	@Column(name = "name")
	private String name;

	/** 개척자 이름 */
	@Column(name = "pioneer_name")
	private String pioneerName;

	/** 좋아요 수 */
	@Column(name = "like_count")
	@Default
	private Long likeCount = 0L;

	/** 조회 수 */
	@Column(name = "view_count")
	@Default
	private Long viewCount = 0L;

	/** 완등자 수 */
	@Column(name = "climber_count")
	@Default
	private Long climberCount = 0L;

	/** 댓글 수 */
	@Column(name = "comment_count")
	@Default
	private Long commentCount = 0L;

	/** 루트 시작 지점 위도 */
	@Column(name = "latitude")
	private Double latitude;

	/** 루트 시작 지점 경도 */
	@Column(name = "longitude")
	private Double longitude;

	/** 난이도 레벨 */
	@Enumerated(EnumType.STRING)
	@Column(name = "route_level")
	private Level routeLevel;

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
}
