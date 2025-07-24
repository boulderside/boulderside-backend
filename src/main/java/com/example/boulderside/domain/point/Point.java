package com.example.boulderside.domain.point;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@Column(name = "index")
	private Integer index;

	/** 포인트 이름 */
	@Column(name = "name")
	private String name;

	/** 포인트 설명 */
	@Column(name = "description")
	private String description;

	/** 추가 설명 */
	@Column(name = "note")
	private String note;

	/** 포인트 이미지 URL 목록 */
	@ElementCollection
	@CollectionTable(
		name = "point_images",
		joinColumns = @JoinColumn(name = "point_id")
	)
	@Column(name = "image_url", length = 2048)
	private List<String> imageUrls = new ArrayList<>();
}
