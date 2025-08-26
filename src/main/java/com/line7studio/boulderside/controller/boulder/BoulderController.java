package com.line7studio.boulderside.controller.boulder;

import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.boulder.BoulderUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boulders")
@RequiredArgsConstructor
public class BoulderController {
	private final BoulderUseCase boulderUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<BoulderPageResponse>> getBoulderPage(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(defaultValue = "LATEST_CREATED") BoulderSortType boulderSortType,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) String subCursor,
		@RequestParam(defaultValue = "5") int size) {
		BoulderPageResponse boulderPageResponse = boulderUseCase.getBoulderPage(userDetails.getUserId(), boulderSortType, cursor, subCursor, size);
		return ResponseEntity.ok(ApiResponse.of(boulderPageResponse));
	}

	@GetMapping("/{boulderId}")
	public ResponseEntity<ApiResponse<BoulderResponse>> getBoulder(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable Long boulderId) {
		BoulderResponse boulder = boulderUseCase.getBoulderById(userDetails.getUserId(), boulderId);
		return ResponseEntity.ok(ApiResponse.of(boulder));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<BoulderResponse>> createBoulder(
		@Valid @RequestBody CreateBoulderRequest request) {
		BoulderResponse boulder = boulderUseCase.createBoulder(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(boulder));
	}

	@PutMapping("/{boulderId}")
	public ResponseEntity<ApiResponse<BoulderResponse>> updateBoulder(@PathVariable Long boulderId,
		@Valid @RequestBody UpdateBoulderRequest request) {
		BoulderResponse boulder = boulderUseCase.updateBoulder(boulderId, request);
		return ResponseEntity.ok(ApiResponse.of(boulder));
	}

	@DeleteMapping("/{boulderId}")
	public ResponseEntity<ApiResponse<Void>> deleteBoulder(@PathVariable Long boulderId) {
		boulderUseCase.deleteBoulder(boulderId);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
