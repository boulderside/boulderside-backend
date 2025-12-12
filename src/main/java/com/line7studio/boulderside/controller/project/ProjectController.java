package com.line7studio.boulderside.controller.project;

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

import com.line7studio.boulderside.application.project.ProjectUseCase;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ValidationException;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.project.request.ProjectRequest;
import com.line7studio.boulderside.controller.project.response.ProjectPageResponse;
import com.line7studio.boulderside.controller.project.response.ProjectResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectUseCase projectUseCase;

	@GetMapping("/{projectId}")
	public ResponseEntity<ApiResponse<ProjectResponse>> getProject(
		@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		ProjectResponse response = projectUseCase.getProject(userDetails.userId(), projectId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping(params = "routeId")
	public ResponseEntity<ApiResponse<ProjectResponse>> getProjectByRoute(
		@RequestParam Long routeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		ProjectResponse response = projectUseCase.getProjectByRoute(userDetails.userId(), routeId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
		@Valid @RequestBody ProjectRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (request.getRouteId() == null) {
			throw new ValidationException(ErrorCode.MISSING_REQUIRED_FIELD);
		}
		ProjectResponse response = projectUseCase.createProject(
			userDetails.userId(), request.getRouteId(), request.getCompleted(), request.getMemo(), request.getAttemptHistories());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PutMapping("/{projectId}")
	public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
		@PathVariable Long projectId,
		@Valid @RequestBody ProjectRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		ProjectResponse response = projectUseCase.updateProject(
			userDetails.userId(), projectId, request.getCompleted(), request.getMemo(), request.getAttemptHistories());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<ApiResponse<Void>> deleteProject(
		@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		projectUseCase.deleteProject(userDetails.userId(), projectId);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@GetMapping("/page")
	public ResponseEntity<ApiResponse<ProjectPageResponse>> getProjectPage(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		ProjectPageResponse response = projectUseCase.getProjectPage(userDetails.userId(), cursor, size);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
