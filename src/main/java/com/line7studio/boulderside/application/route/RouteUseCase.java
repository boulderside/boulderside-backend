package com.line7studio.boulderside.application.route;

import com.line7studio.boulderside.controller.route.request.CreateRouteRequest;
import com.line7studio.boulderside.controller.route.request.UpdateRouteRequest;
import com.line7studio.boulderside.controller.route.response.RoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteResponse;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import com.line7studio.boulderside.domain.aggregate.route.enums.RouteSortType;
import com.line7studio.boulderside.domain.aggregate.route.service.RouteService;
import com.line7studio.boulderside.domain.association.like.service.UserRouteLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteUseCase {
	private final RouteService routeService;
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

		List<RouteResponse> routeResponseList = routeList.stream()
			.map(route -> {
				long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;
				boolean liked = userLikeMap.getOrDefault(route.getId(), false);
				return RouteResponse.of(route, likeCount, liked);
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

        return routeList.stream()
            .map(route -> {
                long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;
                boolean liked = userLikeMap.getOrDefault(route.getId(), false);
                return RouteResponse.of(route, likeCount, liked);
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
		
		return RouteResponse.of(route, likeCount, liked);
	}

    @Transactional
	public RouteResponse createRoute(CreateRouteRequest request) {
		Route route = Route.builder()
			.boulderId(request.getBoulderId())
			.name(request.getName())
			.routeLevel(request.getRouteLevel())
			.likeCount(0L)
			.viewCount(0L)
			.climberCount(0L)
			.commentCount(0L)
			.build();

		Route savedRoute = routeService.createRoute(route);
		return RouteResponse.of(savedRoute, 0L, false);
	}

    @Transactional
	public RouteResponse updateRoute(Long userId, Long routeId, UpdateRouteRequest request) {
		Route routeDetails = Route.builder()
			.boulderId(request.getBoulderId())
			.name(request.getName())
			.routeLevel(request.getRouteLevel())
			.build();

		Route updatedRoute = routeService.updateRoute(routeId, routeDetails);
        boolean liked = userRouteLikeService.existsIsLikedByUserId(routeId, userId);
		long likeCount = updatedRoute.getLikeCount() != null ? updatedRoute.getLikeCount() : 0L;

		return RouteResponse.of(updatedRoute, likeCount, liked);
	}

    @Transactional
	public void deleteRoute(Long routeId) {
		userRouteLikeService.deleteAllLikesByRouteId(routeId);
		routeService.deleteRoute(routeId);
	}
}
