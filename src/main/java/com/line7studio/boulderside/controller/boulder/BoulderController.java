package com.line7studio.boulderside.controller.boulder;

import com.line7studio.boulderside.application.boulder.BoulderUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import com.line7studio.boulderside.domain.feature.boulder.enums.BoulderSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boulders")
@RequiredArgsConstructor
public class BoulderController {
	private final BoulderUseCase boulderUseCase;

	@GetMapping("/page")
	public ResponseEntity<ApiResponse<BoulderPageResponse>> getBoulderPage(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(defaultValue = "LATEST_CREATED") BoulderSortType boulderSortType,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) String subCursor,
		@RequestParam(defaultValue = "5") int size) {
        BoulderPageResponse boulderPageResponse = boulderUseCase.getBoulderPage(userDetails.userId(), boulderSortType, cursor, subCursor, size);
		return ResponseEntity.ok(ApiResponse.of(boulderPageResponse));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<BoulderResponse>>> getAllBoulders(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<BoulderResponse> boulderList = boulderUseCase.getAllBoulders(userDetails.userId());
		return ResponseEntity.ok(ApiResponse.of(boulderList));
	}

	@GetMapping(params = "routeId")
	public ResponseEntity<ApiResponse<BoulderResponse>> getBoulderByRouteId(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam Long routeId) {
		BoulderResponse boulder = boulderUseCase.getBoulderByRouteId(userDetails.userId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(boulder));
	}

	@GetMapping("/{boulderId}")
	public ResponseEntity<ApiResponse<BoulderResponse>> getBoulder(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable Long boulderId) {
		BoulderResponse boulder = boulderUseCase.getBoulderById(userDetails.userId(), boulderId);
		return ResponseEntity.ok(ApiResponse.of(boulder));
	}
}
