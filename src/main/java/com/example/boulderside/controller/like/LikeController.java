package com.example.boulderside.controller.like;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boulderside.application.like.LikeUseCase;
import com.example.boulderside.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {
	private final LikeUseCase likeUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<Void>> likeBoulder() {
		likeUseCase.likeBoulder(10L, 10L);
		return ResponseEntity.ok(ApiResponse.of());
	}
}
