package com.line7studio.boulderside.domain.aggregate.image.service;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.aggregate.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

	@Override
	public List<Image> getImageListByImageDomainTypeAndTargetIdList(ImageDomainType imageDomainType, List<Long> targetIdList) {
		return imageRepository.findByImageDomainTypeAndTargetIdIn(ImageDomainType.BOULDER, targetIdList);
	}

	@Override
	public List<Image> getImageListByImageDomainTypeAndTargetId(ImageDomainType imageDomainType, Long targetId) {
		return imageRepository.findByImageDomainTypeAndTargetId(ImageDomainType.BOULDER, targetId);
	}

	@Override
	public List<Image> createImages(List<Image> imageList) {
		return imageRepository.saveAll(imageList);
	}

	@Override
	public void deleteImagesByImageDomainTypeAndTargetId(ImageDomainType imageDomainType, Long targetId) {
		imageRepository.deleteByImageDomainTypeAndTargetId(ImageDomainType.BOULDER, targetId);
	}
}
