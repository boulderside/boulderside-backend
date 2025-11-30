package com.line7studio.boulderside.domain.feature.route.service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.route.enums.RouteSortType;
import com.line7studio.boulderside.domain.feature.route.repository.RouteQueryRepository;
import com.line7studio.boulderside.domain.feature.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteQueryRepository routeQueryRepository;

    @Override
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public List<Route> getRoutesWithCursor(Long cursor, String subCursor, int size, RouteSortType sortType) {
        return routeQueryRepository.findRoutesWithCursor(sortType, cursor, subCursor, size);
    }

    @Override
    public Route getRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new DomainException(ErrorCode.ROUTE_NOT_FOUND));
    }

    @Override
    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
	public Route updateRoute(Long routeId, Route routeDetails) {
		Route route = getRouteById(routeId);
		Route updatedRoute = Route.builder()
				.id(route.getId())
				.boulderId(routeDetails.getBoulderId() != null ? routeDetails.getBoulderId() : route.getBoulderId())
				.regionId(routeDetails.getRegionId() != null ? routeDetails.getRegionId() : route.getRegionId())
				.sectorId(routeDetails.getSectorId() != null ? routeDetails.getSectorId() : route.getSectorId())
				.name(routeDetails.getName() != null ? routeDetails.getName() : route.getName())
				.pioneerName(routeDetails.getPioneerName() != null ? routeDetails.getPioneerName() : route.getPioneerName())
				.routeLevel(routeDetails.getRouteLevel() != null ? routeDetails.getRouteLevel() : route.getRouteLevel())
				.latitude(routeDetails.getLatitude() != null ? routeDetails.getLatitude() : route.getLatitude())
				.longitude(routeDetails.getLongitude() != null ? routeDetails.getLongitude() : route.getLongitude())
				.likeCount(route.getLikeCount())
				.viewCount(route.getViewCount())
				.climberCount(route.getClimberCount())
				.commentCount(route.getCommentCount())
				.build();
        
        return routeRepository.save(updatedRoute);
    }

    @Override
    public void deleteRoute(Long routeId) {
        Route route = getRouteById(routeId);
        routeRepository.delete(route);
    }
}
