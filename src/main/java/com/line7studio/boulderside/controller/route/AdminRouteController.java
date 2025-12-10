package com.line7studio.boulderside.controller.route;

import com.line7studio.boulderside.application.route.RouteUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.route.request.CreateRouteRequest;
import com.line7studio.boulderside.controller.route.request.UpdateRouteRequest;
import com.line7studio.boulderside.controller.route.response.RouteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/routes")
@RequiredArgsConstructor
public class AdminRouteController {
	private final RouteUseCase routeUseCase;

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
		RouteResponse route = routeUseCase.updateRoute(userDetails.userId(), routeId, request);
		return ResponseEntity.ok(ApiResponse.of(route));
	}

	@DeleteMapping("/{routeId}")
	public ResponseEntity<ApiResponse<Void>> deleteRoute(@PathVariable Long routeId) {
		routeUseCase.deleteRoute(routeId);
		return ResponseEntity.ok(ApiResponse.success());
	}
}

