package com.line7studio.boulderside.application.boulder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderQueryService;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.TargetType;
import com.line7studio.boulderside.domain.aggregate.image.service.ImageService;
import com.line7studio.boulderside.domain.aggregate.region.entity.Region;
import com.line7studio.boulderside.domain.aggregate.region.service.RegionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoulderUseCase {
	private final ImageService imageService;
	private final RegionService regionService;
	private final BoulderService boulderService;
	private final BoulderQueryService boulderQueryService;

	public BoulderPageResponse getBoulderPage(Long cursor, int size) {
		List<BoulderWithRegion> boulderWithRegionList = boulderQueryService.getBoulderWithRegionList(cursor, size);
		List<Long> boulderIdList = boulderWithRegionList.stream().map(BoulderWithRegion::getId).toList();
		List<Image> imageList = imageService.getImageListByTargetTypeAndTargetIdList(TargetType.BOULDER, boulderIdList);

		Map<Long, List<ImageInfo>> boulderImageInfoMap = imageList.stream()
			.collect(Collectors.groupingBy(
				Image::getTargetId,
				Collectors.mapping(ImageInfo::from,
					Collectors.collectingAndThen(Collectors.toList(), list -> {
						list.sort(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)));
						return list;
					}))
			));

		int boulderWithRegionListSize = boulderWithRegionList.size();

		boolean hasNext = boulderWithRegionListSize > size;
		List<BoulderWithRegion> boulderWithRegionPage = boulderWithRegionList.subList(0,
			Math.min(size, boulderWithRegionListSize));

		List<BoulderResponse> boulderResponseList = boulderWithRegionPage.stream()
			.map(b -> {
				List<ImageInfo> imgs = boulderImageInfoMap.getOrDefault(b.getId(), Collections.emptyList());
				return BoulderResponse.of(b, imgs);
			})
			.toList();

		Long nextCursor = hasNext
			? boulderResponseList.getLast().getId()
			: null;

		return BoulderPageResponse.of(boulderResponseList, nextCursor, hasNext,
			Math.min(size, boulderWithRegionListSize));
	}

	public BoulderResponse getBoulderById(Long boulderId) {
		BoulderWithRegion boulderWithRegion = boulderQueryService.getBoulderWithRegion(boulderId);
		List<Image> imageList = imageService.getImageListByTargetTypeAndTargetId(TargetType.BOULDER, boulderId);
		List<ImageInfo> imageInfoList = imageList.stream()
			.map(ImageInfo::from)
			.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
			.toList();

		return BoulderResponse.of(boulderWithRegion, imageInfoList);
	}

	public BoulderResponse createBoulder(CreateBoulderRequest request) {
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		Boulder boulder = Boulder.builder()
			.regionId(region.getId())
			.name(request.getName())
			.description(request.getDescription())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.build();

		Boulder savedBoulder = boulderService.createBoulder(boulder);

		List<ImageInfo> imageInfoList = Collections.emptyList();
		if (request.getImageUrlList() != null && !request.getImageUrlList().isEmpty()) {
			List<Image> newImageList = new ArrayList<>();
			for (int i = 0; i < request.getImageUrlList().size(); i++) {
				Image image = Image.builder()
					.targetId(savedBoulder.getId())
					.targetType(TargetType.BOULDER)
					.imageUrl(request.getImageUrlList().get(i))
					.orderIndex(i)
					.build();
				newImageList.add(image);
			}

			List<Image> savedImages = imageService.createImages(newImageList);
			imageInfoList = savedImages.stream()
				.map(ImageInfo::from)
				.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
				.toList();
		}

		return BoulderResponse.of(savedBoulder, region.getProvince(), region.getCity(), imageInfoList);
	}

	public BoulderResponse updateBoulder(Long boulderId, UpdateBoulderRequest request) {
		Boulder boulder = boulderService.getBoulderById(boulderId);
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		boulder.update(
			region.getId(),
			request.getName(),
			request.getDescription(),
			request.getLatitude(),
			request.getLongitude()
		);

		imageService.deleteImagesByTargetTypeAndTargetId(TargetType.BOULDER, boulderId);

		List<ImageInfo> imageInfoList = Collections.emptyList();
		if (request.getImageUrlList() != null && !request.getImageUrlList().isEmpty()) {
			List<Image> newImageList = new ArrayList<>();
			for (int i = 0; i < request.getImageUrlList().size(); i++) {
				Image image = Image.builder()
					.targetId(boulderId)
					.targetType(TargetType.BOULDER)
					.imageUrl(request.getImageUrlList().get(i))
					.orderIndex(i)
					.build();
				newImageList.add(image);
			}

			List<Image> savedImages = imageService.createImages(newImageList);
			imageInfoList = savedImages.stream()
				.map(ImageInfo::from)
				.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
				.toList();
		}

		return BoulderResponse.of(boulder, region.getProvince(), region.getCity(), imageInfoList);
	}

	public void deleteBoulder(Long boulderId) {
		boulderService.deleteBoulder(boulderId);
	}
}
