package com.line7studio.boulderside.controller.route;

import com.line7studio.boulderside.application.route.RouteUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.route.request.CreateRouteRequest;
import com.line7studio.boulderside.controller.route.request.UpdateRouteRequest;
import com.line7studio.boulderside.controller.route.response.RoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteResponse;
import com.line7studio.boulderside.domain.aggregate.route.enums.RouteSortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {
	private final RouteUseCase routeUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<RoutePageResponse>> getRoutePage(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(defaultValue = "DIFFICULTY") RouteSortType routeSortType,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) String subCursor,
		@RequestParam(defaultValue = "5") int size) {
        RoutePageResponse routePageResponse = routeUseCase.getRoutePage(userDetails.getUserId(), routeSortType, cursor, subCursor, size);
		return ResponseEntity.ok(ApiResponse.of(routePageResponse));
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<RouteResponse>>> getAllRoutes(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<RouteResponse> routeList = routeUseCase.getAllRoutes(userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(routeList));
	}

	@GetMapping("/{routeId}")
	public ResponseEntity<ApiResponse<RouteResponse>> getRoute(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable Long routeId) {
		RouteResponse route = routeUseCase.getRouteById(userDetails.getUserId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(route));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<RouteResponse>> createRoute(
		@Valid @RequestBody CreateRouteRequest request) {
		RouteResponse route = routeUseCase.createRoute(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(route));
	}

	@PutMapping("/{routeId}")
	public ResponseEntity<ApiResponse<RouteResponse>> updateRoute(@PathVariable Long routeId,
		@Valid @RequestBody UpdateRouteRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
		RouteResponse route = routeUseCase.updateRoute(userDetails.getUserId(), routeId, request);
		return ResponseEntity.ok(ApiResponse.of(route));
	}

	@DeleteMapping("/{routeId}")
	public ResponseEntity<ApiResponse<Void>> deleteRoute(@PathVariable Long routeId) {
		routeUseCase.deleteRoute(routeId);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
