package com.line7studio.boulderside.controller.boulder;

import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		@RequestParam(defaultValue = "LATEST") BoulderSortType sortType,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) Long cursorLikeCount,
		@RequestParam(defaultValue = "10") int size) {
		BoulderPageResponse boulderPageResponse = boulderUseCase.getBoulderPage(sortType, cursor, cursorLikeCount, size);
		return ResponseEntity.ok(ApiResponse.of(boulderPageResponse));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BoulderResponse>> getBoulder(@PathVariable Long id) {
		BoulderResponse boulder = boulderUseCase.getBoulderById(id);
		return ResponseEntity.ok(ApiResponse.of(boulder));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<BoulderResponse>> createBoulder(
		@Valid @RequestBody CreateBoulderRequest request) {
		BoulderResponse boulder = boulderUseCase.createBoulder(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(boulder));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<BoulderResponse>> updateBoulder(@PathVariable Long id,
		@Valid @RequestBody UpdateBoulderRequest request) {
		BoulderResponse boulder = boulderUseCase.updateBoulder(id, request);
		return ResponseEntity.ok(ApiResponse.of(boulder));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteBoulder(@PathVariable Long id) {
		boulderUseCase.deleteBoulder(id);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
