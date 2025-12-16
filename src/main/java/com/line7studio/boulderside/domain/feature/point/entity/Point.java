package com.line7studio.boulderside.domain.feature.point.entity;

import com.line7studio.boulderside.domain.BaseEntity;
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
@Table(name = "points")
public class Point extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 진입로 ID (FK) */
	@Column(name = "approach_id", nullable = false)
	private Long approachId;

	/** 포인트 인덱스 */
	@Column(name = "order_index")
	private Integer orderIndex;

	/** 포인트 이름 */
	@Column(name = "name")
	private String name;

	/** 포인트 설명 */
	@Column(name = "description")
	private String description;

	/** 추가 설명 */
	@Column(name = "note")
	private String note;
}
