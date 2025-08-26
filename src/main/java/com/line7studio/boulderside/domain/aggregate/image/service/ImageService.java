package com.line7studio.boulderside.domain.aggregate.image.service;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;

import java.util.List;

public interface ImageService {
	List<Image> getImageListByImageDomainTypeAndTargetIdList(ImageDomainType imageDomainType, List<Long> targetIdList);

	List<Image> getImageListByImageDomainTypeAndTargetId(ImageDomainType imageDomainType, Long targetId);

	List<Image> createImages(List<Image> imageList);

	void deleteImagesByImageDomainTypeAndTargetId(ImageDomainType imageDomainType, Long targetId);
}
