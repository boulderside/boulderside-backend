package com.line7studio.boulderside.controller.route;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.interaction.RouteInteractionUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.interaction.request.CompletionRequest;
import com.line7studio.boulderside.controller.interaction.response.RouteCompletionResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteInteractionController {
	private final RouteInteractionUseCase routeInteractionUseCase;

	@GetMapping("/{routeId}/completion")
	public ResponseEntity<ApiResponse<RouteCompletionResponse>> getCompletion(
		@PathVariable Long routeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		RouteCompletionResponse response = routeInteractionUseCase.getCompletion(userDetails.getUserId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/{routeId}/completion")
	public ResponseEntity<ApiResponse<RouteCompletionResponse>> createCompletion(
		@PathVariable Long routeId,
		@Valid @RequestBody CompletionRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		RouteCompletionResponse response = routeInteractionUseCase.createCompletion(
			userDetails.getUserId(), routeId, request.getCompleted(), request.getMemo());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PutMapping("/{routeId}/completion")
	public ResponseEntity<ApiResponse<RouteCompletionResponse>> updateCompletion(
		@PathVariable Long routeId,
		@Valid @RequestBody CompletionRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		RouteCompletionResponse response = routeInteractionUseCase.updateCompletion(
			userDetails.getUserId(), routeId, request.getCompleted(), request.getMemo());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{routeId}/completion")
	public ResponseEntity<ApiResponse<Void>> deleteCompletion(
		@PathVariable Long routeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		routeInteractionUseCase.deleteCompletion(userDetails.getUserId(), routeId);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@GetMapping("/completions")
	public ResponseEntity<ApiResponse<List<RouteCompletionResponse>>> getCompletionList(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<RouteCompletionResponse> responses = routeInteractionUseCase.getAllCompletions(userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(responses));
	}
}
