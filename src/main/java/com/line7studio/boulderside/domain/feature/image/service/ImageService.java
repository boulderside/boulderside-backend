package com.line7studio.boulderside.domain.feature.image.service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.image.entity.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.feature.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	public List<Image> getAllImages() {
		return imageRepository.findAll();
	}

	public Image getById(Long imageId) {
		return imageRepository.findById(imageId)
			.orElseThrow(() -> new DomainException(ErrorCode.IMAGE_NOT_FOUND));
	}

	public List<Image> getImageListByImageDomainTypeAndDomainIdList(ImageDomainType imageDomainType, List<Long> domainIdList) {
		return imageRepository.findByImageDomainTypeAndDomainIdIn(imageDomainType, domainIdList);
	}

	public List<Image> getImageListByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId) {
		return imageRepository.findByImageDomainTypeAndDomainId(imageDomainType, domainId);
	}

	public List<Image> getImageListByImageDomainTypeAndDomainIdAndOrderIndex(ImageDomainType imageDomainType, Long domainId, Integer orderIndex) {
		return imageRepository.findByImageDomainTypeAndDomainIdAndOrderIndex(imageDomainType, domainId, orderIndex);
	}

	public List<Image> getImageListByImageDomainType(ImageDomainType imageDomainType) {
		return imageRepository.findByImageDomainType(imageDomainType);
	}

	public List<Image> createImages(List<Image> imageList) {
		return imageRepository.saveAll(imageList);
	}

	public Image save(Image image) {
		return imageRepository.save(image);
	}

	public void deleteById(Long imageId) {
		if (!imageRepository.existsById(imageId)) {
			throw new DomainException(ErrorCode.IMAGE_NOT_FOUND);
		}
		imageRepository.deleteById(imageId);
	}

	public void deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId) {
		imageRepository.deleteByImageDomainTypeAndDomainId(imageDomainType, domainId);
	}
}
