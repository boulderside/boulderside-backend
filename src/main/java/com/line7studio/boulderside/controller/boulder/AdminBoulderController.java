package com.line7studio.boulderside.controller.boulder;

import com.line7studio.boulderside.application.boulder.BoulderUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/boulders")
@RequiredArgsConstructor
public class AdminBoulderController {
	private final BoulderUseCase boulderUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<BoulderResponse>> createBoulder(
            @Valid @RequestBody CreateBoulderRequest request) {
        BoulderResponse boulder = boulderUseCase.createBoulder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(boulder));
    }

	@PutMapping("/{boulderId}")
	public ResponseEntity<ApiResponse<BoulderResponse>> updateBoulder(@PathVariable Long boulderId,
		@Valid @RequestBody UpdateBoulderRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		BoulderResponse response = boulderUseCase.updateBoulder(userDetails.getUserId(), boulderId, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{boulderId}")
	public ResponseEntity<ApiResponse<Void>> deleteBoulder(@PathVariable Long boulderId) {
		boulderUseCase.deleteBoulder(boulderId);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
