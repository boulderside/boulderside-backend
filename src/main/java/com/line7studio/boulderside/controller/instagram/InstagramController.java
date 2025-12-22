package com.line7studio.boulderside.controller.instagram;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.instagram.request.CreateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.request.UpdateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.response.InstagramPageResponse;
import com.line7studio.boulderside.controller.instagram.response.InstagramResponse;
import com.line7studio.boulderside.controller.instagram.response.RouteInstagramPageResponse;
import com.line7studio.boulderside.usecase.instagram.InstagramUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instagrams")
@RequiredArgsConstructor
public class InstagramController {
	private final InstagramUseCase instagramUseCase;

	@PostMapping
	public ResponseEntity<ApiResponse<InstagramResponse>> createInstagram(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody CreateInstagramRequest request
	) {
		InstagramResponse response = instagramUseCase.createInstagram(userDetails.userId(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@GetMapping("/{instagramId}")
	public ResponseEntity<ApiResponse<InstagramResponse>> getInstagram(
		@PathVariable Long instagramId
	) {
		InstagramResponse response = instagramUseCase.getInstagram(instagramId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/page")
	public ResponseEntity<ApiResponse<InstagramPageResponse>> getInstagramsPage(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size
	) {
		InstagramPageResponse response = instagramUseCase.getInstagramsPage(cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/my")
	public ResponseEntity<ApiResponse<InstagramPageResponse>> getMyInstagrams(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size
	) {
		InstagramPageResponse response = instagramUseCase.getInstagramsByUserIdPage(userDetails.userId(), cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PutMapping("/{instagramId}")
	public ResponseEntity<ApiResponse<InstagramResponse>> updateInstagram(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long instagramId,
		@Valid @RequestBody UpdateInstagramRequest request
	) {
		InstagramResponse response = instagramUseCase.updateInstagram(userDetails.userId(), instagramId, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{instagramId}")
	public ResponseEntity<ApiResponse<Void>> deleteInstagram(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long instagramId
	) {
		instagramUseCase.deleteInstagram(userDetails.userId(), instagramId);
		return ResponseEntity.ok(ApiResponse.success());
	}

	// Route별 Instagram 조회

	@GetMapping(params = "routeId")
	public ResponseEntity<ApiResponse<RouteInstagramPageResponse>> getInstagramsByRouteId(
		@RequestParam Long routeId,
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size
	) {
		RouteInstagramPageResponse response = instagramUseCase.getInstagramsByRouteIdPage(routeId, cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}