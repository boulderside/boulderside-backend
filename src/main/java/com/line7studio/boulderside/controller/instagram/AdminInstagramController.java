package com.line7studio.boulderside.controller.instagram;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.instagram.request.AdminCreateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.request.AdminUpdateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.response.InstagramPageResponse;
import com.line7studio.boulderside.controller.instagram.response.InstagramResponse;
import com.line7studio.boulderside.controller.instagram.response.RouteInstagramPageResponse;
import com.line7studio.boulderside.usecase.instagram.InstagramUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/instagrams")
@RequiredArgsConstructor
public class AdminInstagramController {
	private final InstagramUseCase instagramUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<List<InstagramResponse>>> getAllInstagrams() {
		List<InstagramResponse> responses = instagramUseCase.getAllInstagrams();
		return ResponseEntity.ok(ApiResponse.of(responses));
	}

	@GetMapping("/page")
	public ResponseEntity<ApiResponse<InstagramPageResponse>> getInstagramsPage(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size
	) {
		InstagramPageResponse response = instagramUseCase.getInstagramsPage(cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/{instagramId}")
	public ResponseEntity<ApiResponse<InstagramResponse>> getInstagram(
		@PathVariable Long instagramId
	) {
		InstagramResponse response = instagramUseCase.getInstagram(instagramId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<InstagramResponse>> createInstagram(
		@Valid @RequestBody AdminCreateInstagramRequest request
	) {
		InstagramResponse response = instagramUseCase.createInstagramByAdmin(request.userId(), request.url(), request.routeIds());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@PutMapping("/{instagramId}")
	public ResponseEntity<ApiResponse<InstagramResponse>> updateInstagram(
		@PathVariable Long instagramId,
		@Valid @RequestBody AdminUpdateInstagramRequest request
	) {
		InstagramResponse response = instagramUseCase.updateInstagramByAdmin(instagramId, request.userId(), request.url(), request.routeIds());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{instagramId}")
	public ResponseEntity<ApiResponse<Void>> deleteInstagram(
		@PathVariable Long instagramId
	) {
		instagramUseCase.deleteInstagramByAdmin(instagramId);
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