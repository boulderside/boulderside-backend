package com.line7studio.boulderside.controller.user;

import com.line7studio.boulderside.domain.user.UserMeta;
import com.line7studio.boulderside.domain.user.enums.ConsentType;
import com.line7studio.boulderside.usecase.user.UserProfileUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.line7studio.boulderside.controller.user.request.WithdrawUserRequest;
import com.line7studio.boulderside.controller.user.request.UpdateConsentRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.user.request.UpdateNicknameRequest;
import com.line7studio.boulderside.controller.user.response.MeResponse;
import com.line7studio.boulderside.controller.user.response.NicknameAvailabilityResponse;
import com.line7studio.boulderside.controller.user.response.ProfileImageResponse;
import com.line7studio.boulderside.controller.user.response.UpdateConsentResponse;
import com.line7studio.boulderside.controller.user.response.UserMetaResponse;
import com.line7studio.boulderside.domain.user.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final UserProfileUseCase userProfileUseCase;

	@PatchMapping("/me/consent")
	public ResponseEntity<ApiResponse<UpdateConsentResponse>> updateConsent(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid UpdateConsentRequest request
	) {
		UserMeta updatedMeta = userService.updateConsent(userDetails.userId(), request);
		boolean updatedValue = resolveConsentValue(updatedMeta, request.consentType());
		UpdateConsentResponse response = UpdateConsentResponse.of(request.consentType(), updatedValue);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MeResponse>> getUserInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(ApiResponse.of(MeResponse.from(userDetails)));
	}

	@GetMapping("/me/meta")
	public ResponseEntity<ApiResponse<UserMetaResponse>> getUserMetaInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		UserMeta userMeta = userService.getUserMeta(userDetails.userId());
		return ResponseEntity.ok(ApiResponse.of(UserMetaResponse.from(userMeta)));
	}

	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> withdrawUser(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid WithdrawUserRequest request
	) {
		userService.withdrawUser(userDetails.userId(), request.reason());
		return ResponseEntity.ok(ApiResponse.success());
	}

	@PatchMapping("/me/nickname")
	public ResponseEntity<ApiResponse<Void>> updateNickname(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid UpdateNicknameRequest request
	) {
		userService.updateNickname(userDetails.userId(), request.nickname());
		return ResponseEntity.ok(ApiResponse.success());
	}

	@PatchMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<ProfileImageResponse>> updateProfileImage(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestPart("profileImage") MultipartFile profileImage
	) {
		ProfileImageResponse response = userProfileUseCase.updateProfileImage(userDetails.userId(), profileImage);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/nickname/availability")
	public ResponseEntity<ApiResponse<NicknameAvailabilityResponse>> checkNicknameAvailability(
		@RequestParam @NotBlank String nickname
	) {
		boolean available = userService.isNicknameAvailable(nickname);
		return ResponseEntity.ok(ApiResponse.of(NicknameAvailabilityResponse.of(nickname, available)));
	}

	private boolean resolveConsentValue(UserMeta userMeta, ConsentType consentType) {
		return switch (consentType) {
			case PUSH -> Boolean.TRUE.equals(userMeta.getPushEnabled());
			case MARKETING -> Boolean.TRUE.equals(userMeta.getMarketingAgreed());
			case PRIVACY -> Boolean.TRUE.equals(userMeta.getPrivacyAgreed());
			case SERVICE_TERMS -> Boolean.TRUE.equals(userMeta.getServiceTermsAgreed());
			case OVER_FOURTEEN -> Boolean.TRUE.equals(userMeta.getOverFourteenAgreed());
		};
	}
}
