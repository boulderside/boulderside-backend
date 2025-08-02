package com.line7studio.boulderside.controller.like;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.like.LikeUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.like.request.LikeRequest;
import com.line7studio.boulderside.controller.like.response.LikeResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
	private final LikeUseCase likeUseCase;

	@PostMapping("/toggle")
	public ResponseEntity<ApiResponse<LikeResponse>> toggleLike(@Valid @RequestBody LikeRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikeResponse response = likeUseCase.toggleBoulderLike(userDetails.getUserId(), request.getBoulderId());
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
