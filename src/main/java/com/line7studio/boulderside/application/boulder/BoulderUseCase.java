package com.line7studio.boulderside.application.boulder;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoulderUseCase {
	private final ImageService imageService;
	private final RegionService regionService;
	private final BoulderService boulderService;
	private final UserBoulderLikeService userBoulderLikeService;

    @Transactional(readOnly = true)
    public BoulderPageResponse getBoulderPage(Long userId, BoulderSortType sortType, Long cursor, String subCursor, int size) {
		List<Boulder> boulderList = boulderService.getBouldersWithCursor(cursor, subCursor, size + 1, sortType);

        boolean hasNext = boulderList.size() > size;
        if (hasNext) {
            boulderList = boulderList.subList(0, size);
        }

		List<Long> boulderIdList = boulderList.stream().map(Boulder::getId).toList();
		
		if (boulderList.isEmpty()) {
			return BoulderPageResponse.of(Collections.emptyList(), null, null, false, 0);
		}

		Map<Long, Boolean> userLikeMap = userBoulderLikeService.getIsLikedByUserIdForBoulderList(boulderIdList, userId);

		List<Long> regionIdList = boulderList.stream().map(Boulder::getRegionId).distinct().toList();
		Map<Long, Region> regionMap = regionService.getRegionsByIds(regionIdList)
			.stream()
			.collect(Collectors.toMap(Region::getId, Function.identity()));

		List<Image> imageList = imageService.getImageListByImageDomainTypeAndDomainIdList(ImageDomainType.BOULDER, boulderIdList);
		Map<Long, List<ImageInfo>> boulderImageInfoMap = imageList.stream()
			.collect(Collectors.groupingBy(
				Image::getDomainId,
				Collectors.mapping(ImageInfo::from,
					Collectors.collectingAndThen(Collectors.toList(), list -> {
						list.sort(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)));
						return list;
					}))
			));

		List<BoulderResponse> boulderResponseList = boulderList.stream()
			.map(boulder -> {
				Region region = regionMap.get(boulder.getRegionId());
				List<ImageInfo> images = boulderImageInfoMap.getOrDefault(boulder.getId(), Collections.emptyList());
				long likeCount = boulder.getLikeCount();
				boolean liked = userLikeMap.get(boulder.getId());
				return BoulderResponse.of(boulder, region.getProvince(), region.getCity(), images, likeCount, liked);
			})
			.toList();

        Long nextCursor = null;
        String nextSubCursor = null;
        if (hasNext && !boulderList.isEmpty()) {
            Boulder lastBoulder = boulderList.getLast();
            nextCursor = lastBoulder.getId();
            nextSubCursor = getNextSubCursor(lastBoulder, sortType);
        }

		return BoulderPageResponse.of(boulderResponseList, nextCursor, nextSubCursor, hasNext, boulderList.size());
	}

	private String getNextSubCursor(Boulder boulder, BoulderSortType sortType) {
		return switch (sortType) {
            case LATEST_CREATED -> boulder.getCreatedAt().toString();
			case MOST_LIKED -> boulder.getLikeCount().toString();
		};
	}

    @Transactional
	public BoulderResponse getBoulderById(Long userId, Long boulderId) {
		Boulder boulder = boulderService.getBoulderById(boulderId);
		Region region = regionService.getRegionById(boulder.getRegionId());
		List<Image> imageList = imageService.getImageListByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, boulderId);
		List<ImageInfo> imageInfoList = imageList.stream()
			.map(ImageInfo::from)
			.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
			.toList();

		boolean liked = userBoulderLikeService.existsIsLikedByUserId(boulderId, userId);

		return BoulderResponse.of(boulder, region.getProvince(), region.getCity(), imageInfoList, boulder.getLikeCount(), liked);
	}

    @Transactional
	public BoulderResponse createBoulder(CreateBoulderRequest request) {
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		Boulder boulder = Boulder.builder()
			.regionId(region.getId())
			.name(request.getName())
			.description(request.getDescription())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.likeCount(0L)
			.build();

		Boulder savedBoulder = boulderService.createBoulder(boulder);

		List<ImageInfo> imageInfoList = Collections.emptyList();
		if (request.getImageUrlList() != null && !request.getImageUrlList().isEmpty()) {
			List<Image> newImageList = new ArrayList<>();
			for (int i = 0; i < request.getImageUrlList().size(); i++) {
				Image image = Image.builder()
                    .imageDomainType(ImageDomainType.BOULDER)
                    .domainId(savedBoulder.getId())
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

    @Transactional
	public BoulderResponse updateBoulder(Long boulderId, UpdateBoulderRequest request) {
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		Boulder boulder = boulderService.updateBoulder(
			boulderId,
			region.getId(),
			request.getName(),
			request.getDescription(),
			request.getLatitude(),
			request.getLongitude()
		);

		imageService.deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, boulderId);

		List<ImageInfo> imageInfoList = Collections.emptyList();
		if (request.getImageUrlList() != null && !request.getImageUrlList().isEmpty()) {
			List<Image> newImageList = new ArrayList<>();
			for (int i = 0; i < request.getImageUrlList().size(); i++) {
				Image image = Image.builder()
                    .imageDomainType(ImageDomainType.BOULDER)
                    .domainId(boulderId)
					.imageUrl(request.getImageUrlList().get(i))
					.orderIndex(i)
					.build();
				newImageList.add(image);
			}

			List<Image> savedImageList = imageService.createImages(newImageList);
			imageInfoList = savedImageList.stream()
				.map(ImageInfo::from)
				.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
				.toList();
		}

		return BoulderResponse.of(boulder, region.getProvince(), region.getCity(), imageInfoList, boulder.getLikeCount());
	}

    @Transactional
	public void deleteBoulder(Long boulderId) {
		imageService.deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, boulderId);
		userBoulderLikeService.deleteAllLikesByBoulderId(boulderId);
		boulderService.deleteByBoulderId(boulderId);
	}
}
