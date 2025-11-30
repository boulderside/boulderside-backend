package com.line7studio.boulderside.domain.feature.image.service;

import com.line7studio.boulderside.domain.feature.image.entity.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.feature.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

	@Override
	public List<Image> getImageListByImageDomainTypeAndDomainIdList(ImageDomainType imageDomainType, List<Long> domainIdList) {
		return imageRepository.findByImageDomainTypeAndDomainIdIn(imageDomainType, domainIdList);
	}

	@Override
	public List<Image> getImageListByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId) {
		return imageRepository.findByImageDomainTypeAndDomainId(imageDomainType, domainId);
	}

	@Override
	public List<Image> getImageListByImageDomainTypeAndDomainIdAndOrderIndex(ImageDomainType imageDomainType, Long domainId, Integer orderIndex) {
		return imageRepository.findByImageDomainTypeAndDomainIdAndOrderIndex(imageDomainType, domainId, orderIndex);
	}

	@Override
	public List<Image> createImages(List<Image> imageList) {
		return imageRepository.saveAll(imageList);
	}

	@Override
	public void deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId) {
		imageRepository.deleteByImageDomainTypeAndDomainId(imageDomainType, domainId);
	}
}
