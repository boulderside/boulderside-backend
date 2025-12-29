package com.line7studio.boulderside.usecase.boulder;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.common.notification.NotificationDomainType;
import com.line7studio.boulderside.common.notification.NotificationTarget;
import com.line7studio.boulderside.common.notification.PushMessage;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.enums.BoulderSortType;
import com.line7studio.boulderside.domain.boulder.interaction.like.service.UserBoulderLikeService;
import com.line7studio.boulderside.domain.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.image.Image;
import com.line7studio.boulderside.domain.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.image.service.ImageService;
import com.line7studio.boulderside.domain.region.Region;
import com.line7studio.boulderside.domain.region.service.RegionService;
import com.line7studio.boulderside.domain.route.service.RouteService;
import com.line7studio.boulderside.domain.user.service.UserService;
import com.line7studio.boulderside.infrastructure.fcm.FcmService;
import com.line7studio.boulderside.common.util.CursorPageUtil;
import com.line7studio.boulderside.common.util.CursorPageUtil.CursorPageWithSubCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
	private final RouteService routeService;
	private final UserBoulderLikeService userBoulderLikeService;
	private final UserService userService;
	private final FcmService fcmService;

	@Transactional(readOnly = true)
	public BoulderPageResponse getBoulderPage(Long userId, BoulderSortType sortType, Long cursor, String subCursor, int size) {
		List<Boulder> boulderList = boulderService.getBouldersWithCursor(cursor, subCursor, size + 1, sortType);

		CursorPageWithSubCursor<Boulder> page = CursorPageUtil.ofWithSubCursor(
			boulderList, size, Boulder::getId, b -> getNextSubCursor(b, sortType));

		if (page.content().isEmpty()) {
			return BoulderPageResponse.of(Collections.emptyList(), null, null, false, 0);
		}

		List<BoulderResponse> boulderResponseList = buildBoulderResponseList(page.content(), userId);

		return BoulderPageResponse.of(boulderResponseList, page.nextCursor(), page.nextSubCursor(), page.hasNext(), page.size());
	}

	@Transactional(readOnly = true)
	public List<BoulderResponse> getAllBoulders(Long userId) {
		List<Boulder> boulderList = boulderService.getAllBoulders();
		if (boulderList.isEmpty()) {
			return Collections.emptyList();
		}
		return buildBoulderResponseList(boulderList, userId);
	}

	@Transactional
	public BoulderResponse getBoulderById(Long userId, Long boulderId) {
		// Service에 조회수 증가 위임
		Boulder boulder = boulderService.incrementViewCount(boulderId);
		return buildSingleBoulderResponse(boulder, userId);
	}

	@Transactional
	public BoulderResponse getBoulderByRouteId(Long userId, Long routeId) {
        Long boulderId = routeService.getById(routeId).getBoulderId();
        Boulder boulder = boulderService.incrementViewCount(boulderId);
        return buildSingleBoulderResponse(boulder, userId);

    }

	@Transactional
	public BoulderResponse createBoulder(CreateBoulderRequest request) {
		// 연관 도메인 조회
		Region region = regionService.getRegionByProvinceAndCity(request.province(), request.city());

		// Request → Domain 변환 (정적 팩토리 메서드 사용)
		Boulder boulder = Boulder.create(
			region.getId(),
			request.name(),
			request.description(),
			request.latitude(),
			request.longitude()
		);

		// 저장
		Boulder savedBoulder = boulderService.save(boulder);
		publishBoulderPushAfterCommit(savedBoulder, region);

		// 이미지 생성 (ImageService에 위임)
		List<Image> images = imageService.createImagesForDomain(
			ImageDomainType.BOULDER, savedBoulder.getId(), request.imageUrlList());
		List<ImageInfo> imageInfoList = toSortedImageInfoList(images);

		// Domain → Response 변환
		return BoulderResponse.of(
			savedBoulder,
			region.getProvince(),
			region.getCity(),
			imageInfoList,
			0L,
			false
		);
	}

	@Transactional
	public BoulderResponse updateBoulder(Long userId, Long boulderId, UpdateBoulderRequest request) {
		// 연관 도메인 조회
		Region region = regionService.getRegionByProvinceAndCity(request.province(), request.city());

		// 업데이트
		Boulder boulder = boulderService.update(
			boulderId,
			region.getId(),
			request.name(),
			request.description(),
			request.latitude(),
			request.longitude()
		);

		// 이미지 교체 (ImageService에 위임)
		List<Image> images = imageService.replaceImagesForDomain(
			ImageDomainType.BOULDER, boulderId, request.imageUrlList());
		List<ImageInfo> imageInfoList = toSortedImageInfoList(images);

		boolean liked = userBoulderLikeService.existsIsLikedByUserId(boulderId, userId);
		long likeCount = Optional.ofNullable(boulder.getLikeCount()).orElse(0L);

		return BoulderResponse.of(
			boulder,
			region.getProvince(),
			region.getCity(),
			imageInfoList,
			likeCount,
			liked
		);
	}

	@Transactional
	public void deleteBoulder(Long boulderId) {
		imageService.deleteAllImagesByImageDomainTypeAndDomainId(ImageDomainType.BOULDER, boulderId);
		userBoulderLikeService.deleteAllLikesByBoulderId(boulderId);
		boulderService.delete(boulderId);
	}

	// === Private Helper Methods ===

	private String getNextSubCursor(Boulder boulder, BoulderSortType sortType) {
		return switch (sortType) {
			case LATEST_CREATED -> boulder.getCreatedAt().toString();
			case MOST_LIKED -> boulder.getLikeCount().toString();
		};
	}

	private List<BoulderResponse> buildBoulderResponseList(List<Boulder> boulderList, Long userId) {
		List<Long> boulderIdList = boulderList.stream().map(Boulder::getId).toList();

		// 좋아요 정보 조회
		Map<Long, Boolean> userLikeMap = userBoulderLikeService.getIsLikedByUserIdForBoulderList(boulderIdList, userId);

		// Region 조회
		List<Long> regionIdList = boulderList.stream().map(Boulder::getRegionId).distinct().toList();
		Map<Long, Region> regionMap = regionService.getRegionsByIds(regionIdList)
			.stream()
			.collect(Collectors.toMap(Region::getId, Function.identity()));

		// 이미지 조회
		Map<Long, List<ImageInfo>> boulderImageInfoMap = getImageInfoMapForBoulders(boulderIdList);

		// Response 조립
		return boulderList.stream()
			.map(boulder -> {
				Region region = regionMap.get(boulder.getRegionId());
				List<ImageInfo> images = boulderImageInfoMap.getOrDefault(boulder.getId(), Collections.emptyList());
				long likeCount = Optional.ofNullable(boulder.getLikeCount()).orElse(0L);
				boolean liked = userLikeMap.getOrDefault(boulder.getId(), false);
				return BoulderResponse.of(
					boulder,
					region.getProvince(),
					region.getCity(),
					images,
					likeCount,
					liked
				);
			})
			.toList();
	}

	private BoulderResponse buildSingleBoulderResponse(Boulder boulder, Long userId) {
		Region region = regionService.getRegionById(boulder.getRegionId());

		List<Image> imageList = imageService.getImageListByImageDomainTypeAndDomainId(
			ImageDomainType.BOULDER, boulder.getId());
		List<ImageInfo> imageInfoList = toSortedImageInfoList(imageList);

		boolean liked = userBoulderLikeService.existsIsLikedByUserId(boulder.getId(), userId);
		long likeCount = Optional.ofNullable(boulder.getLikeCount()).orElse(0L);

		return BoulderResponse.of(
			boulder,
			region.getProvince(),
			region.getCity(),
			imageInfoList,
			likeCount,
			liked
		);
	}

	private Map<Long, List<ImageInfo>> getImageInfoMapForBoulders(List<Long> boulderIdList) {
		List<Image> imageList = imageService.getImageListByImageDomainTypeAndDomainIdList(
			ImageDomainType.BOULDER, boulderIdList);

		return imageList.stream()
			.collect(Collectors.groupingBy(
				Image::getDomainId,
				Collectors.mapping(ImageInfo::from,
					Collectors.collectingAndThen(Collectors.toList(), list -> {
						list.sort(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)));
						return list;
					}))
			));
	}

	private List<ImageInfo> toSortedImageInfoList(List<Image> images) {
		return images.stream()
			.map(ImageInfo::from)
			.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
			.toList();
	}

	private void publishBoulderPushAfterCommit(Boulder boulder, Region region) {
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			sendBoulderPush(boulder, region);
			return;
		}
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				sendBoulderPush(boulder, region);
			}
		});
	}

	private void sendBoulderPush(Boulder boulder, Region region) {
		List<String> tokens = userService.getAllFcmTokens();
		NotificationTarget target = new NotificationTarget(NotificationDomainType.BOULDER, String.valueOf(boulder.getId()));
		String location = region.getProvince() + (region.getCity() != null ? " " + region.getCity() : "");
		String body = location + "에 새 바위 '" + boulder.getName() + "'이 등록되었습니다";
		PushMessage message = new PushMessage("새 바위 등록", body, target);
		fcmService.sendMessageToAll(tokens, message);
	}
}
