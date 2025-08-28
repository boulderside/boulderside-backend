package com.line7studio.boulderside.domain.aggregate.route.service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import com.line7studio.boulderside.domain.aggregate.route.repository.RouteRepository;
import com.line7studio.boulderside.infrastructure.elasticsearch.service.ElasticsearchSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final ElasticsearchSyncService elasticsearchSyncService;

    @Override
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public Route getRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new DomainException(ErrorCode.ROUTE_NOT_FOUND));
    }

    @Override
    public Route createRoute(Route route) {
        Route savedRoute = routeRepository.save(route);
        elasticsearchSyncService.syncRoute(savedRoute);
        return savedRoute;
    }

    @Override
    public Route updateRoute(Long routeId, Route routeDetails) {
        Route route = getRouteById(routeId);
        // Update route fields - Route entity doesn't have update methods, so we need to create a new one
        Route updatedRoute = Route.builder()
                .id(route.getId())
                .boulderId(routeDetails.getBoulderId() != null ? routeDetails.getBoulderId() : route.getBoulderId())
                .name(routeDetails.getName() != null ? routeDetails.getName() : route.getName())
                .routeLevel(routeDetails.getRouteLevel() != null ? routeDetails.getRouteLevel() : route.getRouteLevel())
                .build();
        
        Route savedRoute = routeRepository.save(updatedRoute);
        elasticsearchSyncService.syncRoute(savedRoute);
        return savedRoute;
    }

    @Override
    public void deleteRoute(Long routeId) {
        Route route = getRouteById(routeId);
        routeRepository.delete(route);
        elasticsearchSyncService.deleteRoute(routeId);
    }
}