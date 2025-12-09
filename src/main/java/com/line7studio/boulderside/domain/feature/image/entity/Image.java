package com.line7studio.boulderside.domain.feature.image.entity;

import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;

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

	/** 업로드된 원본 파일명 */
	@Column(name = "original_filename")
	private String originalFileName;

	/** 연관 대상 ID (FK) */
	@Column(name = "domain_id", nullable = false)
	private Long domainId;

	/** 연관 대상 타입 */
	@Enumerated(EnumType.STRING)
	@Column(name = "image_domain_type", nullable = false)
	private ImageDomainType imageDomainType;

	/** 이미지 URL */
	@Column(name = "image_url", length = 2048)
	private String imageUrl;

	/** 이미지 인덱스 (순서) */
	@Column(name = "order_index")
	private Integer orderIndex;

	public void update(Long domainId, ImageDomainType imageDomainType, String imageUrl, Integer orderIndex,
		String originalFileName) {
		this.domainId = domainId;
		this.imageDomainType = imageDomainType;
		this.imageUrl = imageUrl;
		this.orderIndex = orderIndex;
		this.originalFileName = originalFileName;
	}
}
