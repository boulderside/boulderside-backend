package com.line7studio.boulderside.domain.aggregate.image.entity;

import com.line7studio.boulderside.domain.aggregate.image.enums.TargetType;

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
@Table(name = "images")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 대상 ID (FK) */
	@Column(name = "target_id", nullable = false)
	private Long targetId;

	/** 연관 대상 타입 */
	@Enumerated(EnumType.STRING)
	@Column(name = "target_type", nullable = false)
	private TargetType targetType;

	/** 이미지 URL */
	@Column(name = "image_url", length = 2048)
	private String imageUrl;

	/** 이미지 인덱스 (순서) */
	@Column(name = "order_index")
	private Integer orderIndex;
}
