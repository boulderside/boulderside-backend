package com.line7studio.boulderside.controller.matepost;

import com.line7studio.boulderside.application.post.MatePostUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.matepost.request.CreateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.request.UpdateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.response.MatePostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/mate-posts")
@RequiredArgsConstructor
public class AdminMatePostController {

    private final MatePostUseCase matePostUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<MatePostResponse>> createMatePost(
        @Valid @RequestBody CreateMatePostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostResponse response = matePostUseCase.adminCreateMatePost(request, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<MatePostResponse>> updateMatePost(
        @PathVariable Long postId,
        @Valid @RequestBody UpdateMatePostRequest request
    ) {
        MatePostResponse response = matePostUseCase.adminUpdateMatePost(postId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteMatePost(@PathVariable Long postId) {
        matePostUseCase.adminDeleteMatePost(postId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
