package com.line7studio.boulderside.domain.feature.route.service;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.route.entity.Route;
import com.line7studio.boulderside.domain.feature.route.enums.RouteSortType;
import com.line7studio.boulderside.domain.feature.route.repository.RouteQueryRepository;
import com.line7studio.boulderside.domain.feature.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteQueryRepository routeQueryRepository;

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Route getById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new DomainException(ErrorCode.ROUTE_NOT_FOUND));
    }

    /**
     * 조회수를 증가시키고 업데이트된 Route를 반환합니다.
     */
    public Route incrementViewCount(Long routeId) {
        Route route = getById(routeId);
        route.incrementViewCount();
        return route;
    }

    public List<Route> getRoutesWithCursor(Long cursor, String subCursor, int size, RouteSortType sortType) {
        return routeQueryRepository.findRoutesWithCursor(sortType, cursor, subCursor, size);
    }

    public List<Route> getRoutesByIds(List<Long> routeIds) {
        return routeRepository.findAllById(routeIds);
    }

    public List<Route> getRoutesByBoulderId(Long boulderId) {
        return routeRepository.findByBoulderIdOrderByIdAsc(boulderId);
    }

    /**
     * Route를 저장합니다.
     */
    public Route save(Route route) {
        return routeRepository.save(route);
    }

    /**
     * Route 정보를 업데이트합니다.
     */
    public Route update(Long routeId, Long boulderId, Long regionId, Long sectorId,
                        String name, String description, Level routeLevel,
                        String pioneerName, Double latitude, Double longitude) {
        Route route = getById(routeId);
        route.update(boulderId, regionId, sectorId, name, description, routeLevel, pioneerName, latitude, longitude);
        return route;
    }

    /**
     * Route를 삭제합니다.
     */
    public void delete(Long routeId) {
        Route route = getById(routeId);
        routeRepository.delete(route);
    }
}