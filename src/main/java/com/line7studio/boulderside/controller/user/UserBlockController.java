package com.line7studio.boulderside.controller.user;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.user.request.BlockUserRequest;
import com.line7studio.boulderside.controller.user.response.BlockedUserResponse;
import com.line7studio.boulderside.usecase.user.UserBlockUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/me/blocks")
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockUseCase userBlockUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlockedUserResponse>>> getBlockedUsers(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<BlockedUserResponse> responses = userBlockUseCase.getBlockedUsers(userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> blockUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody @Valid BlockUserRequest request
    ) {
        userBlockUseCase.blockUser(userDetails.userId(), request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{blockedUserId}")
    public ResponseEntity<ApiResponse<Void>> unblockUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long blockedUserId
    ) {
        userBlockUseCase.unblockUser(userDetails.userId(), blockedUserId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
