package com.line7studio.boulderside.domain.aggregate.route.service;

import com.line7studio.boulderside.domain.aggregate.route.Route;

import java.util.List;

public interface RouteService {
    List<Route> getAllRoutes();
    Route getRouteById(Long routeId);
    Route createRoute(Route route);
    Route updateRoute(Long routeId, Route routeDetails);
    void deleteRoute(Long routeId);
}