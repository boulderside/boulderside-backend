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
	public List<Image> getImageListByImageDomainTypeAndDomainIdList(ImageDomainType imageDomainType, List<Long> domainIdList) {
		return imageRepository.findByImageDomainTypeAndDomainIdIn(ImageDomainType.BOULDER, domainIdList);
	}

	@Override
	public List<Image> getImageListByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId) {
		return imageRepository.findByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, domainId);
	}

	@Override
	public List<Image> createImages(List<Image> imageList) {
		return imageRepository.saveAll(imageList);
	}

	@Override
	public void deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId) {
		imageRepository.deleteByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, domainId);
	}
}
