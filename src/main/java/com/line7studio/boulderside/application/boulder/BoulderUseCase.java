package com.line7studio.boulderside.application.boulder;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.enums.BoulderSortType;
import com.line7studio.boulderside.domain.feature.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.feature.image.entity.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.feature.image.service.ImageService;
import com.line7studio.boulderside.domain.feature.region.entity.Region;
import com.line7studio.boulderside.domain.feature.region.service.RegionService;
import com.line7studio.boulderside.domain.feature.sector.Sector;
import com.line7studio.boulderside.domain.feature.sector.service.SectorService;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.service.UserBoulderLikeService;
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
	private final SectorService sectorService;
	private final BoulderService boulderService;
	private final RouteService routeService;
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

		List<Long> sectorIdList = boulderList.stream().map(Boulder::getSectorId).distinct().toList();
		Map<Long, Sector> sectorMap = sectorService.getSectorsByIds(sectorIdList).stream()
			.collect(Collectors.toMap(Sector::getId, Function.identity()));

		List<BoulderResponse> boulderResponseList = boulderList.stream()
			.map(boulder -> {
				Region region = regionMap.get(boulder.getRegionId());
				Sector sector = sectorMap.get(boulder.getSectorId());
				List<ImageInfo> images = boulderImageInfoMap.getOrDefault(boulder.getId(), Collections.emptyList());
				long likeCount = Optional.ofNullable(boulder.getLikeCount()).orElse(0L);
				boolean liked = userLikeMap.getOrDefault(boulder.getId(), false);
				return BoulderResponse.of(
					boulder,
					region.getProvince(),
					region.getCity(),
					sector != null ? sector.getSectorName() : null,
					sector != null ? sector.getAreaCode() : null,
					images,
					likeCount,
					liked
				);
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

    @Transactional(readOnly = true)
    public List<BoulderResponse> getAllBoulders(Long userId) {
        List<Boulder> boulderList = boulderService.getAllBoulders();
        if (boulderList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> boulderIdList = boulderList.stream()
            .map(Boulder::getId)
            .toList();

        Map<Long, Boolean> userLikeMap = userBoulderLikeService.getIsLikedByUserIdForBoulderList(boulderIdList, userId);

        List<Long> regionIdList = boulderList.stream()
            .map(Boulder::getRegionId)
            .distinct()
            .toList();
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

        List<Long> sectorIdList = boulderList.stream()
            .map(Boulder::getSectorId)
            .distinct()
            .toList();
        Map<Long, Sector> sectorMap = sectorService.getSectorsByIds(sectorIdList)
            .stream()
            .collect(Collectors.toMap(Sector::getId, Function.identity()));

        return boulderList.stream()
            .map(boulder -> {
                Region region = regionMap.get(boulder.getRegionId());
                Sector sector = sectorMap.get(boulder.getSectorId());
                List<ImageInfo> images = boulderImageInfoMap.getOrDefault(boulder.getId(), Collections.emptyList());
                long likeCount = Optional.ofNullable(boulder.getLikeCount()).orElse(0L);
                boolean liked = userLikeMap.getOrDefault(boulder.getId(), false);
                return BoulderResponse.of(
                    boulder,
                    region.getProvince(),
                    region.getCity(),
                    sector != null ? sector.getSectorName() : null,
                    sector != null ? sector.getAreaCode() : null,
                    images,
                    likeCount,
                    liked
                );
            })
            .toList();
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
		boulder.incrementViewCount();
		Region region = regionService.getRegionById(boulder.getRegionId());
		Sector sector = sectorService.getSectorById(boulder.getSectorId());
		List<Image> imageList = imageService.getImageListByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, boulderId);
		List<ImageInfo> imageInfoList = imageList.stream()
			.map(ImageInfo::from)
			.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
			.toList();

		boolean liked = userBoulderLikeService.existsIsLikedByUserId(boulderId, userId);
		long likeCount = Optional.ofNullable(boulder.getLikeCount()).orElse(0L);

		return BoulderResponse.of(
			boulder,
			region.getProvince(),
			region.getCity(),
			sector.getSectorName(),
			sector.getAreaCode(),
			imageInfoList,
			likeCount,
			liked
		);
	}

	@Transactional
	public BoulderResponse getBoulderByRouteId(Long userId, Long routeId) {
		Long boulderId = routeService.getRouteById(routeId).getBoulderId();
		return getBoulderById(userId, boulderId);
	}

    @Transactional
	public BoulderResponse createBoulder(CreateBoulderRequest request) {
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		Sector sector = sectorService.getSectorById(request.getSectorId());

		Boulder boulder = Boulder.builder()
			.regionId(region.getId())
			.sectorId(request.getSectorId())
			.name(request.getName())
			.description(request.getDescription())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.likeCount(0L)
			.viewCount(0L)
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

		return BoulderResponse.of(
			savedBoulder,
			region.getProvince(),
			region.getCity(),
			sector.getSectorName(),
			sector.getAreaCode(),
			imageInfoList,
			0L,
			false
		);
	}

    @Transactional
	public BoulderResponse updateBoulder(Long userId, Long boulderId, UpdateBoulderRequest request) {
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		Sector sector = sectorService.getSectorById(request.getSectorId());

		Boulder boulder = boulderService.updateBoulder(
			boulderId,
			region.getId(),
			request.getSectorId(),
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

        boolean liked = userBoulderLikeService.existsIsLikedByUserId(boulderId, userId);

		long likeCount = Optional.ofNullable(boulder.getLikeCount()).orElse(0L);
		return BoulderResponse.of(
			boulder,
			region.getProvince(),
			region.getCity(),
			sector.getSectorName(),
			sector.getAreaCode(),
			imageInfoList,
			likeCount,
			liked
		);
	}

    @Transactional
	public void deleteBoulder(Long boulderId) {
		imageService.deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, boulderId);
		userBoulderLikeService.deleteAllLikesByBoulderId(boulderId);
		boulderService.deleteByBoulderId(boulderId);

	}
}
