package com.example.boulderside.domain.point;

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
@Table(name = "points")
public class Point {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 진입로 ID (FK) */
	@Column(name = "approach_id", nullable = false)
	private Long approachId;

	/** 포인트 인덱스 */
	@Column(name = "sequence")
	private Integer sequence;

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
