package com.line7studio.boulderside.domain.route.repository;

import java.util.List;

import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.enums.RouteSortType;

public interface RouteQueryRepository {
	List<Route> findRoutesWithCursor(RouteSortType sortType, Long cursor, String subCursor, int size);
}