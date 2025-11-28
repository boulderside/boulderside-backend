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

	/** 루트 이름 */
	@Column(name = "name")
	private String name;

    /** 좋아요 수 */
    @Column(name = "like_count")
    private Long likeCount;

    /** 조회 수 */
    @Column(name = "view_count")
    private Long viewCount;

    /** 조회 수 */
    @Column(name = "climber_count")
    private Long climberCount;

    /** 댓글 수 */
    @Column(name = "comment_count")
    private Long commentCount;

    /** 난이도 레벨 */
	@Enumerated(EnumType.STRING)
	@Column(name = "route_level")
	private Level routeLevel;

	public void incrementViewCount() {
		this.viewCount = this.viewCount + 1;
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
