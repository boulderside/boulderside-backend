package com.line7studio.boulderside.controller.image.response;

import com.line7studio.boulderside.domain.feature.image.entity.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminImageResponse {
	private Long id;
	private Long domainId;
	private ImageDomainType imageDomainType;
	private String imageUrl;
	private String originalFileName;
	private Integer orderIndex;

	public static AdminImageResponse from(Image image) {
		return AdminImageResponse.builder()
			.id(image.getId())
			.domainId(image.getDomainId())
			.imageDomainType(image.getImageDomainType())
			.imageUrl(image.getImageUrl())
			.originalFileName(image.getOriginalFileName())
			.orderIndex(image.getOrderIndex())
			.build();
	}
}
