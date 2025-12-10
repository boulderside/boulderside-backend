package com.line7studio.boulderside.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.user.request.UpdateNicknameRequest;
import com.line7studio.boulderside.controller.user.response.MeResponse;
import com.line7studio.boulderside.controller.user.response.NicknameAvailabilityResponse;
import com.line7studio.boulderside.domain.feature.user.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MeResponse>> getUserInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(ApiResponse.of(MeResponse.from(userDetails)));
	}

	@PatchMapping("/me/nickname")
	public ResponseEntity<ApiResponse<Void>> updateNickname(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid UpdateNicknameRequest request
	) {
		userService.updateNickname(userDetails.userId(), request.nickname());
		return ResponseEntity.ok(ApiResponse.success());
	}

	@GetMapping("/nickname/availability")
	public ResponseEntity<ApiResponse<NicknameAvailabilityResponse>> checkNicknameAvailability(
		@RequestParam @NotBlank String nickname
	) {
		boolean available = userService.isNicknameAvailable(nickname);
		return ResponseEntity.ok(ApiResponse.of(NicknameAvailabilityResponse.of(nickname, available)));
	}
}
