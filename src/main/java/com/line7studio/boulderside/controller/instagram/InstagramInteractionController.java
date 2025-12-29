package com.line7studio.boulderside.controller.instagram;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.usecase.interaction.InstagramInteractionUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.instagram.response.InstagramLikeResponse;
import com.line7studio.boulderside.controller.instagram.response.LikedInstagramPageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/instagrams")
@RequiredArgsConstructor
public class InstagramInteractionController {
	private final InstagramInteractionUseCase instagramInteractionUseCase;

	@PostMapping("/{instagramId}/likes/toggle")
	public ResponseEntity<ApiResponse<InstagramLikeResponse>> toggleInstagramLike(
		@PathVariable Long instagramId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		InstagramLikeResponse response = instagramInteractionUseCase.toggleLike(userDetails.userId(), instagramId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/likes")
	public ResponseEntity<ApiResponse<LikedInstagramPageResponse>> getLikedInstagrams(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikedInstagramPageResponse response = instagramInteractionUseCase.getLikedInstagrams(userDetails.userId(), cursor,
			size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}