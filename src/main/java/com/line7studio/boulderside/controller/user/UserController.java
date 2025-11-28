package com.line7studio.boulderside.controller.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.line7studio.boulderside.application.user.UserUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.user.request.ChangePasswordRequest;
import com.line7studio.boulderside.controller.user.request.FindIdByPhoneRequest;
import com.line7studio.boulderside.controller.user.request.PhoneAuthRequest;
import com.line7studio.boulderside.controller.user.request.PhoneLinkRequest;
import com.line7studio.boulderside.controller.user.request.PhoneLookupRequest;
import com.line7studio.boulderside.controller.user.request.SignupRequest;
import com.line7studio.boulderside.controller.user.request.VerifyCodeRequest;
import com.line7studio.boulderside.controller.user.response.FindIdByPhoneResponse;
import com.line7studio.boulderside.controller.user.response.MeResponse;
import com.line7studio.boulderside.controller.user.response.PhoneLookupResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserUseCase userUseCase;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MeResponse>> getUserInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(ApiResponse.of(MeResponse.from(userDetails)));
	}

	@GetMapping("/check-id")
	public ResponseEntity<ApiResponse<Boolean>> checkUserId(@RequestParam String username) {
		boolean isDuplicate = userUseCase.isUserIdDuplicate(username);
		return ResponseEntity.ok(ApiResponse.of(!isDuplicate));
	}

	@PostMapping("/phone/send-code")
	public ResponseEntity<ApiResponse<Void>> sendPhoneAuthCode(@RequestBody PhoneAuthRequest request) {
		userUseCase.sendAuthCode(request.phoneNumber());
		return ResponseEntity.ok(ApiResponse.success());
	}

	@PostMapping("/phone/verify-code")
	public ResponseEntity<ApiResponse<Boolean>> verifyPhoneAuthCode(@RequestBody VerifyCodeRequest request) {
		boolean response = userUseCase.verifyAuthCode(request.phoneNumber(), request.code());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/phone/lookup")
	public ResponseEntity<ApiResponse<PhoneLookupResponse>> checkPhone(
		@RequestBody PhoneLookupRequest request) {
		PhoneLookupResponse response = userUseCase.lookupUserByPhone(request.phoneNumber());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/phone/link-account")
	public ResponseEntity<ApiResponse<Void>> linkPhoneAccount(
		@RequestBody PhoneLinkRequest request) {

		userUseCase.linkAccountByPhone(request);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Void>> signUp(
		@Valid @RequestPart("data") SignupRequest request,
		@RequestPart(value = "file", required = false) MultipartFile file
	) {
		userUseCase.signUp(request, file);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@PostMapping("/find-id")
	public ResponseEntity<ApiResponse<FindIdByPhoneResponse>> findIdByPhone(
		@RequestBody @Valid FindIdByPhoneRequest request
	) {
		FindIdByPhoneResponse response = userUseCase.findIdByPhone(request.phoneNumber());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/change-password")
	public ResponseEntity<ApiResponse<Void>> changePassword(
		@Valid @RequestBody ChangePasswordRequest request) {
		userUseCase.changePassword(request.phoneNumber(), request.newPassword());
		return ResponseEntity.ok(ApiResponse.success());
	}
}
