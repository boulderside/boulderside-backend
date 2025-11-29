package com.line7studio.boulderside.application.route;

import com.line7studio.boulderside.controller.route.request.CreateRouteRequest;
import com.line7studio.boulderside.controller.route.request.UpdateRouteRequest;
import com.line7studio.boulderside.controller.route.response.RoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.aggregate.region.entity.Region;
import com.line7studio.boulderside.domain.aggregate.region.service.RegionService;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import com.line7studio.boulderside.domain.aggregate.route.enums.RouteSortType;
import com.line7studio.boulderside.domain.aggregate.route.service.RouteService;
import com.line7studio.boulderside.domain.aggregate.sector.Sector;
import com.line7studio.boulderside.domain.aggregate.sector.service.SectorService;
import com.line7studio.boulderside.domain.association.like.service.UserRouteLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteUseCase {
	private final RouteService routeService;
	private final BoulderService boulderService;
	private final RegionService regionService;
	private final SectorService sectorService;
	private final UserRouteLikeService userRouteLikeService;

    @Transactional(readOnly = true)
    public RoutePageResponse getRoutePage(Long userId, RouteSortType sortType, Long cursor, String subCursor, int size) {
		List<Route> routeList = routeService.getRoutesWithCursor(cursor, subCursor, size + 1, sortType);

        boolean hasNext = routeList.size() > size;
        if (hasNext) {
            routeList = routeList.subList(0, size);
        }

		List<Long> routeIdList = routeList.stream().map(Route::getId).toList();
		
		if (routeList.isEmpty()) {
			return RoutePageResponse.of(Collections.emptyList(), null, null, false, 0);
		}

		Map<Long, Boolean> userLikeMap = userRouteLikeService.getIsLikedByUserIdForRouteList(routeIdList, userId);

		List<Long> regionIdList = routeList.stream().map(Route::getRegionId).distinct().toList();
		Map<Long, Region> regionMap = regionService.getRegionsByIds(regionIdList).stream()
			.collect(Collectors.toMap(Region::getId, Function.identity()));

		List<Long> sectorIdList = routeList.stream().map(Route::getSectorId).distinct().toList();
		Map<Long, Sector> sectorMap = sectorService.getSectorsByIds(sectorIdList).stream()
			.collect(Collectors.toMap(Sector::getId, Function.identity()));

		List<RouteResponse> routeResponseList = routeList.stream()
			.map(route -> {
				long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;
				boolean liked = userLikeMap.getOrDefault(route.getId(), false);
				Region region = regionMap.get(route.getRegionId());
				Sector sector = sectorMap.get(route.getSectorId());
				return RouteResponse.of(
					route,
					region != null ? region.getProvince() : null,
					region != null ? region.getCity() : null,
					sector != null ? sector.getSectorName() : null,
					sector != null ? sector.getAreaCode() : null,
					likeCount,
					liked
				);
			})
			.toList();

        Long nextCursor = null;
        String nextSubCursor = null;
        if (hasNext && !routeList.isEmpty()) {
            Route lastRoute = routeList.getLast();
            nextCursor = lastRoute.getId();
            nextSubCursor = getNextSubCursor(lastRoute, sortType);
        }

		return RoutePageResponse.of(routeResponseList, nextCursor, nextSubCursor, hasNext, routeList.size());
	}

    @Transactional(readOnly = true)
    public List<RouteResponse> getAllRoutes(Long userId) {
        List<Route> routeList = routeService.getAllRoutes();
        if (routeList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> routeIdList = routeList.stream()
            .map(Route::getId)
            .toList();

        Map<Long, Boolean> userLikeMap = userRouteLikeService.getIsLikedByUserIdForRouteList(routeIdList, userId);

		List<Long> regionIdList = routeList.stream()
			.map(Route::getRegionId)
			.distinct()
			.toList();
		Map<Long, Region> regionMap = regionService.getRegionsByIds(regionIdList)
			.stream()
			.collect(Collectors.toMap(Region::getId, Function.identity()));

		List<Long> sectorIdList = routeList.stream()
			.map(Route::getSectorId)
			.distinct()
			.toList();
		Map<Long, Sector> sectorMap = sectorService.getSectorsByIds(sectorIdList)
			.stream()
			.collect(Collectors.toMap(Sector::getId, Function.identity()));

        return routeList.stream()
            .map(route -> {
                long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;
                boolean liked = userLikeMap.getOrDefault(route.getId(), false);
				Region region = regionMap.get(route.getRegionId());
				Sector sector = sectorMap.get(route.getSectorId());
                return RouteResponse.of(
					route,
					region != null ? region.getProvince() : null,
					region != null ? region.getCity() : null,
					sector != null ? sector.getSectorName() : null,
					sector != null ? sector.getAreaCode() : null,
					likeCount,
					liked
				);
            })
            .toList();
    }

	private String getNextSubCursor(Route route, RouteSortType sortType) {
		return switch (sortType) {
            case DIFFICULTY -> route.getRouteLevel() != null ? route.getRouteLevel().toString() : "";
            case MOST_LIKED -> route.getLikeCount() != null ? route.getLikeCount().toString() : "0";
            case MOST_CLIMBERS -> route.getClimberCount() != null ? route.getClimberCount().toString() : "0";
		};
	}

	@Transactional
	public RouteResponse getRouteById(Long userId, Long routeId) {
		Route route = routeService.getRouteById(routeId);
		route.incrementViewCount();
		boolean liked = userRouteLikeService.existsIsLikedByUserId(routeId, userId);
		long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;
		Region region = regionService.getRegionById(route.getRegionId());
		Sector sector = sectorService.getSectorById(route.getSectorId());
		
		return RouteResponse.of(
			route,
			region.getProvince(),
			region.getCity(),
			sector.getSectorName(),
			sector.getAreaCode(),
			likeCount,
			liked
		);
	}

	@Transactional
	public RouteResponse createRoute(CreateRouteRequest request) {
		Boulder boulder = boulderService.getBoulderById(request.getBoulderId());
		Region region = regionService.getRegionById(boulder.getRegionId());
		Sector sector = sectorService.getSectorById(boulder.getSectorId());

		Route route = Route.builder()
			.boulderId(request.getBoulderId())
			.regionId(boulder.getRegionId())
			.sectorId(boulder.getSectorId())
			.name(request.getName())
			.pioneerName(request.getPioneerName())
			.routeLevel(request.getRouteLevel())
			.latitude(boulder.getLatitude())
			.longitude(boulder.getLongitude())
			.likeCount(0L)
			.viewCount(0L)
			.climberCount(0L)
			.commentCount(0L)
			.build();

		Route savedRoute = routeService.createRoute(route);
		return RouteResponse.of(
			savedRoute,
			region.getProvince(),
			region.getCity(),
			sector.getSectorName(),
			sector.getAreaCode(),
			0L,
			false
		);
	}

	@Transactional
	public RouteResponse updateRoute(Long userId, Long routeId, UpdateRouteRequest request) {
		Boulder boulder = boulderService.getBoulderById(request.getBoulderId());
		Region region = regionService.getRegionById(boulder.getRegionId());
		Sector sector = sectorService.getSectorById(boulder.getSectorId());

		Route routeDetails = Route.builder()
			.boulderId(request.getBoulderId())
			.regionId(boulder.getRegionId())
			.sectorId(boulder.getSectorId())
			.name(request.getName())
			.pioneerName(request.getPioneerName())
			.routeLevel(request.getRouteLevel())
			.latitude(boulder.getLatitude())
			.longitude(boulder.getLongitude())
			.build();

		Route updatedRoute = routeService.updateRoute(routeId, routeDetails);
        boolean liked = userRouteLikeService.existsIsLikedByUserId(routeId, userId);
		long likeCount = updatedRoute.getLikeCount() != null ? updatedRoute.getLikeCount() : 0L;

		return RouteResponse.of(
			updatedRoute,
			region.getProvince(),
			region.getCity(),
			sector.getSectorName(),
			sector.getAreaCode(),
			likeCount,
			liked
		);
	}

    @Transactional
	public void deleteRoute(Long routeId) {
		userRouteLikeService.deleteAllLikesByRouteId(routeId);
		routeService.deleteRoute(routeId);
	}
}
