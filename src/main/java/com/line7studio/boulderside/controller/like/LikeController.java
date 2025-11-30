package com.line7studio.boulderside.controller.like;

import com.line7studio.boulderside.application.like.LikeUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.like.response.LikeResponse;
import com.line7studio.boulderside.controller.like.response.LikedBoulderPageResponse;
import com.line7studio.boulderside.controller.like.response.LikedRoutePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
	private final LikeUseCase likeUseCase;

	@PostMapping("/boulders/{boulderId}/toggle")
	public ResponseEntity<ApiResponse<LikeResponse>> toggleBoulderLike(@PathVariable Long boulderId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikeResponse response = likeUseCase.toggleBoulderLike(userDetails.getUserId(), boulderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/routes/{routeId}/toggle")
	public ResponseEntity<ApiResponse<LikeResponse>> toggleRouteLike(@PathVariable Long routeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikeResponse response = likeUseCase.toggleRouteLike(userDetails.getUserId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/boulders")
	public ResponseEntity<ApiResponse<LikedBoulderPageResponse>> getLikedBoulders(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikedBoulderPageResponse response = likeUseCase.getLikedBoulders(userDetails.getUserId(), cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/routes")
	public ResponseEntity<ApiResponse<LikedRoutePageResponse>> getLikedRoutes(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikedRoutePageResponse response = likeUseCase.getLikedRoutes(userDetails.getUserId(), cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
