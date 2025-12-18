package com.line7studio.boulderside.controller.matepost;

import com.line7studio.boulderside.usecase.post.MatePostUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.common.request.UpdatePostStatusRequest;
import com.line7studio.boulderside.controller.matepost.request.CreateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.request.UpdateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.response.MatePostPageResponse;
import com.line7studio.boulderside.controller.matepost.response.MatePostResponse;
import com.line7studio.boulderside.domain.mate.enums.MatePostSortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/mate-posts")
@RequiredArgsConstructor
public class AdminMatePostController {

    private final MatePostUseCase matePostUseCase;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<MatePostPageResponse>> getMatePostPage(
        @RequestParam(required = false) Long cursor,
        @RequestParam(required = false) String subCursor,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "LATEST_CREATED") MatePostSortType postSortType,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostPageResponse response = matePostUseCase.getMatePostPageForAdmin(cursor, subCursor, size, postSortType, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<MatePostResponse>> getMatePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostResponse response = matePostUseCase.getMatePostForAdmin(postId, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MatePostResponse>> createMatePost(
        @Valid @RequestBody CreateMatePostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostResponse response = matePostUseCase.adminCreateMatePost(request, userDetails.userId());
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

    @PatchMapping("/{postId}/status")
    public ResponseEntity<ApiResponse<Void>> updateMatePostStatus(
        @PathVariable Long postId,
        @Valid @RequestBody UpdatePostStatusRequest request
    ) {
        matePostUseCase.updateMatePostStatus(postId, request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
