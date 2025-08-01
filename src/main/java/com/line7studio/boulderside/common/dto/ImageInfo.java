package com.line7studio.boulderside.common.dto;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.TargetType;

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
	private TargetType targetType;
	private String imageUrl;
	private Integer orderIndex;

	public static ImageInfo from(Image img) {
		return ImageInfo.builder()
			.targetType(img.getTargetType())
			.imageUrl(img.getImageUrl())
			.orderIndex(img.getOrderIndex())
			.build();
	}
}
