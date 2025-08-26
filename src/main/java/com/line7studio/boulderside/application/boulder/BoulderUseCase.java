package com.line7studio.boulderside.application.boulder;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderQueryService;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.aggregate.image.service.ImageService;
import com.line7studio.boulderside.domain.aggregate.region.entity.Region;
import com.line7studio.boulderside.domain.aggregate.region.service.RegionService;
import com.line7studio.boulderside.domain.association.like.service.UserBoulderLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoulderUseCase {
	private final ImageService imageService;
	private final RegionService regionService;
	private final BoulderService boulderService;
	private final BoulderQueryService boulderQueryService;
	private final UserBoulderLikeService userBoulderLikeService;

	public BoulderPageResponse getBoulderPage(Long userId, BoulderSortType sortType, Long cursor, Long cursorLikeCount, int size) {
		List<BoulderWithRegion> boulderWithRegionList = boulderQueryService.getBoulderWithRegionList(sortType, cursor, cursorLikeCount, size);
		List<Long> boulderIdList = boulderWithRegionList.stream().map(BoulderWithRegion::getId).toList();
		List<Image> imageList = imageService.getImageListByImageDomainTypeAndTargetIdList(ImageDomainType.BOULDER, boulderIdList);

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
				long likeCount = userBoulderLikeService.getCountByBoulderId(b.getId());
				boolean liked = userBoulderLikeService.getIsLikedByUserId(b.getId(), userId);
				return BoulderResponse.of(b, imgs, likeCount, liked);
			})
			.toList();

		Long nextCursor = null;
		Long nextCursorLikeCount = null;
		if(hasNext){
			nextCursor = boulderResponseList.getLast().getId();
			nextCursorLikeCount = boulderResponseList.getLast().getLikeCount();
		}

		return BoulderPageResponse.of(boulderResponseList, nextCursor, nextCursorLikeCount, hasNext,
			Math.min(size, boulderWithRegionListSize));
	}

	public BoulderResponse getBoulderById(Long userId, Long boulderId) {
		BoulderWithRegion boulderWithRegion = boulderQueryService.getBoulderWithRegion(boulderId);
		List<Image> imageList = imageService.getImageListByImageDomainTypeAndTargetId(ImageDomainType.BOULDER, boulderId);
		List<ImageInfo> imageInfoList = imageList.stream()
			.map(ImageInfo::from)
			.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
			.toList();

		long likeCount = userBoulderLikeService.getCountByBoulderId(boulderId);
		boolean liked = userBoulderLikeService.getIsLikedByUserId(boulderId, userId);

		return BoulderResponse.of(boulderWithRegion, imageInfoList, likeCount, liked);
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
					.imageDomainType(ImageDomainType.BOULDER)
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

		return BoulderResponse.of(savedBoulder, region.getProvince(), region.getCity(), imageInfoList, 0L);
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

		long likeCount = userBoulderLikeService.getCountByBoulderId(boulderId);

		imageService.deleteImagesByImageDomainTypeAndTargetId(ImageDomainType.BOULDER, boulderId);

		List<ImageInfo> imageInfoList = Collections.emptyList();
		if (request.getImageUrlList() != null && !request.getImageUrlList().isEmpty()) {
			List<Image> newImageList = new ArrayList<>();
			for (int i = 0; i < request.getImageUrlList().size(); i++) {
				Image image = Image.builder()
					.targetId(boulderId)
					.imageDomainType(ImageDomainType.BOULDER)
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

		return BoulderResponse.of(boulder, region.getProvince(), region.getCity(), imageInfoList, likeCount);
	}

	public void deleteBoulder(Long boulderId) {
		imageService.deleteImagesByImageDomainTypeAndTargetId(ImageDomainType.BOULDER, boulderId);
		userBoulderLikeService.deleteAllByBoulderId(boulderId);
		boulderService.deleteByBoulderId(boulderId);
	}
}
