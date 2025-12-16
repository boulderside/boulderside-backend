package com.line7studio.boulderside.domain.feature.route.repository;

import java.util.List;

import com.line7studio.boulderside.domain.feature.route.entity.Route;
import com.line7studio.boulderside.domain.feature.route.enums.RouteSortType;

public interface RouteQueryRepository {
	List<Route> findRoutesWithCursor(RouteSortType sortType, Long cursor, String subCursor, int size);
}