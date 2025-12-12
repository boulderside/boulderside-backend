package com.line7studio.boulderside.controller.route;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.interaction.RouteInteractionUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.route.response.LikedRoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteLikeResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteInteractionController {
	private final RouteInteractionUseCase routeInteractionUseCase;

	@PostMapping("/{routeId}/likes/toggle")
	public ResponseEntity<ApiResponse<RouteLikeResponse>> toggleRouteLike(
		@PathVariable Long routeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		RouteLikeResponse response = routeInteractionUseCase.toggleLike(userDetails.userId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/likes")
	public ResponseEntity<ApiResponse<LikedRoutePageResponse>> getLikedRoutes(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikedRoutePageResponse response = routeInteractionUseCase.getLikedRoutes(userDetails.userId(), cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
