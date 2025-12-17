package com.line7studio.boulderside.usecase.image;

import com.line7studio.boulderside.controller.image.request.CreateAdminImageRequest;
import com.line7studio.boulderside.controller.image.request.UpdateAdminImageRequest;
import com.line7studio.boulderside.controller.image.response.AdminImageResponse;
import com.line7studio.boulderside.domain.feature.image.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.feature.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageUseCase {

	private final ImageService imageService;

	public List<AdminImageResponse> getImages(ImageDomainType domainType, Long domainId) {
		List<Image> images;
		if (domainType != null && domainId != null) {
			images = imageService.getImageListByImageDomainTypeAndDomainId(domainType, domainId);
		} else if (domainType != null) {
			images = imageService.getImageListByImageDomainType(domainType);
		} else if (domainId != null) {
			images = imageService.getAllImages().stream()
				.filter(image -> domainId.equals(image.getDomainId()))
				.toList();
		} else {
			images = imageService.getAllImages();
		}
		return images.stream()
			.map(AdminImageResponse::from)
			.toList();
	}

	public AdminImageResponse getImage(Long imageId) {
		return AdminImageResponse.from(imageService.getById(imageId));
	}

	@Transactional
	public AdminImageResponse createImage(CreateAdminImageRequest request) {
		Image image = Image.builder()
			.domainId(request.domainId())
			.imageDomainType(request.imageDomainType())
			.imageUrl(request.imageUrl())
			.orderIndex(request.orderIndex())
			.originalFileName(request.originalFileName())
			.build();
		Image saved = imageService.save(image);
		return AdminImageResponse.from(saved);
	}

	@Transactional
	public AdminImageResponse updateImage(Long imageId, UpdateAdminImageRequest request) {
		Image image = imageService.getById(imageId);
		image.update(request.domainId(), request.imageDomainType(), request.imageUrl(), request.orderIndex(),
			request.originalFileName());
		return AdminImageResponse.from(image);
	}

	@Transactional
	public void deleteImage(Long imageId) {
		imageService.deleteById(imageId);
	}
}
