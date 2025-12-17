package com.line7studio.boulderside.usecase.route;

import com.line7studio.boulderside.controller.route.request.CreateRouteRequest;
import com.line7studio.boulderside.controller.route.request.UpdateRouteRequest;
import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.controller.route.response.RoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteResponse;
import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.feature.image.entity.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.feature.image.service.ImageService;
import com.line7studio.boulderside.domain.feature.region.entity.Region;
import com.line7studio.boulderside.domain.feature.region.service.RegionService;
import com.line7studio.boulderside.domain.feature.route.entity.Route;
import com.line7studio.boulderside.domain.feature.route.enums.RouteSortType;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;
import com.line7studio.boulderside.domain.feature.sector.entity.Sector;
import com.line7studio.boulderside.domain.feature.sector.service.SectorService;
import com.line7studio.boulderside.domain.feature.route.interaction.like.service.UserRouteLikeService;
import com.line7studio.boulderside.common.util.CursorPageUtil;
import com.line7studio.boulderside.common.util.CursorPageUtil.CursorPageWithSubCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteUseCase {
	private final RouteService routeService;
	private final BoulderService boulderService;
	private final ImageService imageService;
	private final RegionService regionService;
	private final SectorService sectorService;
	private final UserRouteLikeService userRouteLikeService;

	@Transactional(readOnly = true)
	public RoutePageResponse getRoutePage(Long userId, RouteSortType sortType, Long cursor, String subCursor, int size) {
		List<Route> routeList = routeService.getRoutesWithCursor(cursor, subCursor, size + 1, sortType);

		CursorPageWithSubCursor<Route> page = CursorPageUtil.ofWithSubCursor(
			routeList, size, Route::getId, r -> getNextSubCursor(r, sortType));

		if (page.content().isEmpty()) {
			return RoutePageResponse.of(Collections.emptyList(), null, null, false, 0);
		}

		List<RouteResponse> routeResponseList = buildRouteResponseList(page.content(), userId);

		return RoutePageResponse.of(routeResponseList, page.nextCursor(), page.nextSubCursor(), page.hasNext(), page.size());
	}

	@Transactional(readOnly = true)
	public List<RouteResponse> getRoutes(Long userId, Long boulderId) {
		List<Route> routeList = boulderId == null
			? routeService.getAllRoutes()
			: routeService.getRoutesByBoulderId(boulderId);
		return buildRouteResponseList(routeList, userId);
	}

	@Transactional
	public RouteResponse getRouteById(Long userId, Long routeId) {
		// Service에 조회수 증가 위임
		Route route = routeService.incrementViewCount(routeId);
		return buildSingleRouteResponse(route, userId);
	}

	@Transactional
	public RouteResponse createRoute(CreateRouteRequest request) {
		// 연관 도메인 조회
		Boulder boulder = boulderService.getById(request.boulderId());
		Region region = regionService.getRegionById(boulder.getRegionId());
		Sector sector = sectorService.getSectorById(boulder.getSectorId());

		// Request → Domain 변환 (정적 팩토리 메서드 사용)
		Route route = Route.create(
			request.boulderId(),
			boulder.getRegionId(),
			boulder.getSectorId(),
			request.name(),
			null, // description
			request.routeLevel(),
			request.pioneerName(),
			null, // firstAscentYear
			boulder.getLatitude(),
			boulder.getLongitude()
		);

		// 저장
		Route savedRoute = routeService.save(route);

		// Domain → Response 변환
		return RouteResponse.of(
			savedRoute,
			region.getProvince(),
			region.getCity(),
			boulder.getName(),
			sector.getSectorName(),
			sector.getAreaCode(),
			Collections.emptyList(),
			0L,
			false
		);
	}

	@Transactional
	public RouteResponse updateRoute(Long userId, Long routeId, UpdateRouteRequest request) {
		// 연관 도메인 조회
		Boulder boulder = boulderService.getById(request.boulderId());
		Region region = regionService.getRegionById(boulder.getRegionId());
		Sector sector = sectorService.getSectorById(boulder.getSectorId());

		// 업데이트
		Route updatedRoute = routeService.update(
			routeId,
			request.boulderId(),
			boulder.getRegionId(),
			boulder.getSectorId(),
			request.name(),
			null, // description
			request.routeLevel() != null ? request.routeLevel().name() : null,
			request.pioneerName(),
			null, // firstAscentYear
			boulder.getLatitude(),
			boulder.getLongitude()
		);

		boolean liked = userRouteLikeService.existsIsLikedByUserId(routeId, userId);
		long likeCount = updatedRoute.getLikeCount() != null ? updatedRoute.getLikeCount() : 0L;
		List<ImageInfo> imageInfoList = getImageInfoList(routeId);

		return RouteResponse.of(
			updatedRoute,
			region.getProvince(),
			region.getCity(),
			boulder.getName(),
			sector.getSectorName(),
			sector.getAreaCode(),
			imageInfoList,
			likeCount,
			liked
		);
	}

	@Transactional
	public void deleteRoute(Long routeId) {
		userRouteLikeService.deleteAllLikesByRouteId(routeId);
		routeService.delete(routeId);
	}

	// === Private Helper Methods ===

	private String getNextSubCursor(Route route, RouteSortType sortType) {
		return switch (sortType) {
			case DIFFICULTY -> route.getRouteLevel() != null ? route.getRouteLevel().toString() : "";
			case MOST_LIKED -> route.getLikeCount() != null ? route.getLikeCount().toString() : "0";
			case MOST_CLIMBERS -> route.getClimberCount() != null ? route.getClimberCount().toString() : "0";
		};
	}

	private List<RouteResponse> buildRouteResponseList(List<Route> routeList, Long userId) {
		if (routeList.isEmpty()) {
			return Collections.emptyList();
		}

		List<Long> routeIdList = routeList.stream().map(Route::getId).toList();

		// 좋아요 정보 조회
		Map<Long, Boolean> userLikeMap = userRouteLikeService.getIsLikedByUserIdForRouteList(routeIdList, userId);

		// 이미지 조회
		Map<Long, List<ImageInfo>> routeImageInfoMap = getImageInfoMapForRoutes(routeIdList);

		// Boulder 조회
		List<Long> boulderIdList = routeList.stream().map(Route::getBoulderId).distinct().toList();
		Map<Long, Boulder> boulderMap = boulderService.getBouldersByIds(boulderIdList).stream()
			.collect(Collectors.toMap(Boulder::getId, Function.identity()));

		// Region 조회
		List<Long> regionIdList = routeList.stream().map(Route::getRegionId).distinct().toList();
		Map<Long, Region> regionMap = regionService.getRegionsByIds(regionIdList).stream()
			.collect(Collectors.toMap(Region::getId, Function.identity()));

		// Sector 조회
		List<Long> sectorIdList = routeList.stream().map(Route::getSectorId).distinct().toList();
		Map<Long, Sector> sectorMap = sectorService.getSectorsByIds(sectorIdList).stream()
			.collect(Collectors.toMap(Sector::getId, Function.identity()));

		// Response 조립
		return routeList.stream()
			.map(route -> {
				long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;
				boolean liked = userLikeMap.getOrDefault(route.getId(), false);
				Region region = regionMap.get(route.getRegionId());
				Boulder boulder = boulderMap.get(route.getBoulderId());
				Sector sector = sectorMap.get(route.getSectorId());
				List<ImageInfo> imageInfoList = routeImageInfoMap.getOrDefault(route.getId(), Collections.emptyList());
				return RouteResponse.of(
					route,
					region != null ? region.getProvince() : null,
					region != null ? region.getCity() : null,
					boulder != null ? boulder.getName() : null,
					sector != null ? sector.getSectorName() : null,
					sector != null ? sector.getAreaCode() : null,
					imageInfoList,
					likeCount,
					liked
				);
			})
			.toList();
	}

	private RouteResponse buildSingleRouteResponse(Route route, Long userId) {
		Boulder boulder = boulderService.getById(route.getBoulderId());
		Region region = regionService.getRegionById(route.getRegionId());
		Sector sector = sectorService.getSectorById(route.getSectorId());

		boolean liked = userRouteLikeService.existsIsLikedByUserId(route.getId(), userId);
		long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;
		List<ImageInfo> imageInfoList = getImageInfoList(route.getId());

		return RouteResponse.of(
			route,
			region.getProvince(),
			region.getCity(),
			boulder.getName(),
			sector.getSectorName(),
			sector.getAreaCode(),
			imageInfoList,
			likeCount,
			liked
		);
	}

	private Map<Long, List<ImageInfo>> getImageInfoMapForRoutes(List<Long> routeIdList) {
		List<Image> imageList = imageService.getImageListByImageDomainTypeAndDomainIdList(
			ImageDomainType.ROUTE, routeIdList);

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

	private List<ImageInfo> getImageInfoList(Long routeId) {
		return imageService.getImageListByImageDomainTypeAndDomainId(ImageDomainType.ROUTE, routeId)
			.stream()
			.map(ImageInfo::from)
			.sorted(Comparator.comparing(img -> Optional.ofNullable(img.getOrderIndex()).orElse(0)))
			.toList();
	}
}