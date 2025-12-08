package com.line7studio.boulderside.controller.image;

import com.line7studio.boulderside.application.image.ImageUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.image.request.CreateAdminImageRequest;
import com.line7studio.boulderside.controller.image.request.UpdateAdminImageRequest;
import com.line7studio.boulderside.controller.image.response.AdminImageResponse;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("/admin/images")
@RequiredArgsConstructor
public class AdminImageController {

	private final ImageUseCase imageUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<List<AdminImageResponse>>> getImages(
		@RequestParam(value = "domainType", required = false) ImageDomainType domainType,
		@RequestParam(value = "domainId", required = false) Long domainId) {
		List<AdminImageResponse> responses = imageUseCase.getImages(domainType, domainId);
		return ResponseEntity.ok(ApiResponse.of(responses));
	}

	@GetMapping("/{imageId}")
	public ResponseEntity<ApiResponse<AdminImageResponse>> getImage(@PathVariable Long imageId) {
		return ResponseEntity.ok(ApiResponse.of(imageUseCase.getImage(imageId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<AdminImageResponse>> createImage(
		@Valid @RequestBody CreateAdminImageRequest request) {
		AdminImageResponse response = imageUseCase.createImage(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@PutMapping("/{imageId}")
	public ResponseEntity<ApiResponse<AdminImageResponse>> updateImage(
		@PathVariable Long imageId,
		@Valid @RequestBody UpdateAdminImageRequest request) {
		AdminImageResponse response = imageUseCase.updateImage(imageId, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{imageId}")
	public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long imageId) {
		imageUseCase.deleteImage(imageId);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
