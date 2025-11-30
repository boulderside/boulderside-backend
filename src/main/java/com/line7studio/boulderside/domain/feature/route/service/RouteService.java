package com.line7studio.boulderside.domain.feature.route.service;

import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.route.enums.RouteSortType;

import java.util.List;

public interface RouteService {
    List<Route> getAllRoutes();
    List<Route> getRoutesWithCursor(Long cursor, String subCursor, int size, RouteSortType sortType);
    List<Route> getRoutesByIds(List<Long> routeIds);
    Route getRouteById(Long routeId);
    Route createRoute(Route route);
    Route updateRoute(Long routeId, Route routeDetails);
    void deleteRoute(Long routeId);
}
