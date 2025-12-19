package com.line7studio.boulderside.controller.completion;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.completion.request.CompletionRequest;
import com.line7studio.boulderside.controller.completion.response.CompletionPageResponse;
import com.line7studio.boulderside.controller.completion.response.CompletionResponse;
import com.line7studio.boulderside.usecase.completion.CompletionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/completions")
@RequiredArgsConstructor
@Slf4j
public class CompletionController {
	private final CompletionUseCase completionUseCase;

	@GetMapping("/{completionId}")
	public ResponseEntity<ApiResponse<CompletionResponse>> getCompletion(
		@PathVariable Long completionId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		CompletionResponse response = completionUseCase.getCompletion(userDetails.userId(), completionId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping(params = "routeId")
	public ResponseEntity<ApiResponse<CompletionResponse>> getCompletionByRoute(
		@RequestParam Long routeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		CompletionResponse response = completionUseCase.getCompletionByRoute(userDetails.userId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/date/{date}")
	public ResponseEntity<ApiResponse<List<CompletionResponse>>> getCompletionsByDate(
		@PathVariable LocalDate date,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<CompletionResponse> response = completionUseCase.getCompletionsByDate(userDetails.userId(), date);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/level/{level}")
	public ResponseEntity<ApiResponse<List<CompletionResponse>>> getCompletionsByLevel(
		@PathVariable Level level,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info(level.toString());
		List<CompletionResponse> response = completionUseCase.getCompletionsByLevel(userDetails.userId(), level);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CompletionResponse>> createCompletion(
		@Valid @RequestBody CompletionRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		CompletionResponse response = completionUseCase.createCompletion(userDetails.userId(), request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PutMapping("/{completionId}")
	public ResponseEntity<ApiResponse<CompletionResponse>> updateCompletion(
		@PathVariable Long completionId,
		@Valid @RequestBody CompletionRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		CompletionResponse response = completionUseCase.updateCompletion(userDetails.userId(), completionId, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{completionId}")
	public ResponseEntity<ApiResponse<Void>> deleteCompletion(
		@PathVariable Long completionId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		completionUseCase.deleteCompletion(userDetails.userId(), completionId);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@GetMapping("/page")
	public ResponseEntity<ApiResponse<CompletionPageResponse>> getCompletionPage(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		CompletionPageResponse response = completionUseCase.getCompletionPage(userDetails.userId(), cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
