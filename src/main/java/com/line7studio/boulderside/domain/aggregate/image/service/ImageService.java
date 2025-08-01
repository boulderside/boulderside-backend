package com.line7studio.boulderside.domain.aggregate.image.service;

import java.util.List;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.TargetType;

public interface ImageService {
	List<Image> getImageListByTargetTypeAndTargetIdList(TargetType targetType, List<Long> targetIdList);

	List<Image> getImageListByTargetTypeAndTargetId(TargetType targetType, Long targetId);

	List<Image> createImages(List<Image> imageList);

	void deleteImagesByTargetTypeAndTargetId(TargetType targetType, Long targetId);
}
