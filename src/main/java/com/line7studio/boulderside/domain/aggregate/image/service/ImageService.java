package com.line7studio.boulderside.domain.aggregate.image.service;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;

import java.util.List;

public interface ImageService {
	List<Image> getImageListByImageDomainTypeAndDomainIdList(ImageDomainType imageDomainType, List<Long> targetIdList);

	List<Image> getImageListByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long targetId);

	List<Image> createImages(List<Image> imageList);

	void deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long targetId);
}
