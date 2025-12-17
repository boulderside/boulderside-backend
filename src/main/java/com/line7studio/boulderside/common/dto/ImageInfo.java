package com.line7studio.boulderside.common.dto;

import com.line7studio.boulderside.domain.image.Image;
import com.line7studio.boulderside.domain.image.enums.ImageDomainType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfo {
	private ImageDomainType imageDomainType;
    private Long domainId;
	private String imageUrl;
	private Integer orderIndex;

	public static ImageInfo from(Image img) {
		return ImageInfo.builder()
			.imageDomainType(img.getImageDomainType())
            .domainId(img.getDomainId())
			.imageUrl(img.getImageUrl())
			.orderIndex(img.getOrderIndex())
			.build();
	}
}
