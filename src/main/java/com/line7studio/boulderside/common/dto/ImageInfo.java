package com.line7studio.boulderside.common.dto;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfo {
	// Image
	private ImageDomainType imageDomainType;
	private String imageUrl;
	private Integer orderIndex;

	public static ImageInfo from(Image img) {
		return ImageInfo.builder()
			.imageDomainType(img.getImageDomainType())
			.imageUrl(img.getImageUrl())
			.orderIndex(img.getOrderIndex())
			.build();
	}
}
