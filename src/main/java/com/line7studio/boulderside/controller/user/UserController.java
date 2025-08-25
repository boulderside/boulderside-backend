package com.line7studio.boulderside.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.user.UserUseCase;
import com.line7studio.boulderside.application.user.dto.response.UserInfoResponse;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.user.request.LinkAccountRequest;
import com.line7studio.boulderside.controller.user.request.PhoneAuthRequest;
import com.line7studio.boulderside.controller.user.request.VerifyCodeRequest;
import com.line7studio.boulderside.controller.user.response.LinkAccountResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserUseCase userUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		UserInfoResponse userInfoResponse = userUseCase.getUserInfo(userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(userInfoResponse));
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

	@PostMapping("/phone/link-account")
	public ResponseEntity<ApiResponse<LinkAccountResponse>> linkPhoneAccount(
		@RequestBody LinkAccountRequest request) {
		LinkAccountResponse response = userUseCase.linkAccountWithPhone(request.phoneNumber());
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
