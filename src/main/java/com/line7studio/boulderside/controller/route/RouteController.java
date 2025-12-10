package com.line7studio.boulderside.controller.route;

import com.line7studio.boulderside.application.route.RouteUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.route.response.RoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteResponse;
import com.line7studio.boulderside.domain.feature.route.enums.RouteSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {
	private final RouteUseCase routeUseCase;

	@GetMapping("/page")
	public ResponseEntity<ApiResponse<RoutePageResponse>> getRoutePage(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(defaultValue = "DIFFICULTY") RouteSortType routeSortType,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) String subCursor,
		@RequestParam(defaultValue = "5") int size) {
        RoutePageResponse routePageResponse = routeUseCase.getRoutePage(userDetails.userId(), routeSortType, cursor, subCursor, size);
		return ResponseEntity.ok(ApiResponse.of(routePageResponse));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<RouteResponse>>> getRoutes(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(required = false) Long boulderId) {
		List<RouteResponse> routeList = routeUseCase.getRoutes(userDetails.userId(), boulderId);
		return ResponseEntity.ok(ApiResponse.of(routeList));
	}

	@GetMapping("/{routeId}")
	public ResponseEntity<ApiResponse<RouteResponse>> getRoute(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable Long routeId) {
		RouteResponse route = routeUseCase.getRouteById(userDetails.userId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(route));
	}
}
