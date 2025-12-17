package com.line7studio.boulderside.domain.feature.image.service;

import com.line7studio.boulderside.common.exception.BusinessException;
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
			.orElseThrow(() -> new BusinessException(ErrorCode.IMAGE_NOT_FOUND));
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

	/**
	 * URL 리스트로 특정 도메인에 연결된 이미지들을 생성합니다.
	 * @param imageDomainType 이미지가 연결될 도메인 타입
	 * @param domainId 도메인 ID
	 * @param imageUrls 이미지 URL 리스트
	 * @return 생성된 이미지 리스트
	 */
	public List<Image> createImagesForDomain(ImageDomainType imageDomainType, Long domainId, List<String> imageUrls) {
		if (imageUrls == null || imageUrls.isEmpty()) {
			return List.of();
		}

		List<Image> images = new java.util.ArrayList<>();
		for (int i = 0; i < imageUrls.size(); i++) {
			Image image = Image.builder()
				.imageDomainType(imageDomainType)
				.domainId(domainId)
				.imageUrl(imageUrls.get(i))
				.orderIndex(i)
				.build();
			images.add(image);
		}

		return imageRepository.saveAll(images);
	}

	/**
	 * 특정 도메인의 이미지를 모두 교체합니다.
	 * 기존 이미지 삭제 후 새 이미지 생성
	 */
	public List<Image> replaceImagesForDomain(ImageDomainType imageDomainType, Long domainId, List<String> imageUrls) {
		deleteAllImagesByImageDomainTypeAndDomainId(imageDomainType, domainId);
		return createImagesForDomain(imageDomainType, domainId, imageUrls);
	}

	public Image save(Image image) {
		return imageRepository.save(image);
	}

	public void deleteById(Long imageId) {
		if (!imageRepository.existsById(imageId)) {
			throw new BusinessException(ErrorCode.IMAGE_NOT_FOUND);
		}
		imageRepository.deleteById(imageId);
	}

	public void deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId) {
		imageRepository.deleteByImageDomainTypeAndDomainId(imageDomainType, domainId);
	}
}
