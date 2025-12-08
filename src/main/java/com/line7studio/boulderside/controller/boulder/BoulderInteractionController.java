package com.line7studio.boulderside.controller.boulder;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.interaction.BoulderInteractionUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.boulder.response.BoulderLikeResponse;
import com.line7studio.boulderside.controller.boulder.response.LikedBoulderPageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boulders")
@RequiredArgsConstructor
public class BoulderInteractionController {
	private final BoulderInteractionUseCase boulderInteractionUseCase;

	@PostMapping("/{boulderId}/likes/toggle")
	public ResponseEntity<ApiResponse<BoulderLikeResponse>> toggleBoulderLike(
		@PathVariable Long boulderId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		BoulderLikeResponse response = boulderInteractionUseCase.toggleLike(userDetails.getUserId(), boulderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/likes")
	public ResponseEntity<ApiResponse<LikedBoulderPageResponse>> getLikedBoulders(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikedBoulderPageResponse response = boulderInteractionUseCase.getLikedBoulders(userDetails.getUserId(), cursor,
			size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
