package com.line7studio.boulderside.controller.matepost;

import com.line7studio.boulderside.application.post.MatePostUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.matepost.request.CreateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.request.UpdateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.response.MatePostPageResponse;
import com.line7studio.boulderside.controller.matepost.response.MatePostResponse;
import com.line7studio.boulderside.domain.feature.post.enums.MatePostSortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mate-posts")
@RequiredArgsConstructor
@Slf4j
public class MatePostController {

    private final MatePostUseCase matePostUseCase;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<MatePostPageResponse>> getMatePostPage(
        @RequestParam(required = false) Long cursor,
        @RequestParam(required = false) String subCursor,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "LATEST_CREATED") MatePostSortType postSortType,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostPageResponse response = matePostUseCase.getMatePostPage(cursor, subCursor, size, postSortType, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MatePostPageResponse>> getMyMatePosts(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "10") int size,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostPageResponse response = matePostUseCase.getMyMatePosts(cursor, size, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MatePostResponse>> getMatePost(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("조회한 게시글" + id);
        MatePostResponse response = matePostUseCase.getMatePost(id, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MatePostResponse>> createMatePost(
        @Valid @RequestBody CreateMatePostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostResponse response = matePostUseCase.createMatePost(request, userDetails.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MatePostResponse>> updateMatePost(
        @PathVariable Long id,
        @Valid @RequestBody UpdateMatePostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MatePostResponse response = matePostUseCase.updateMatePost(id, request, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMatePost(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        matePostUseCase.deleteMatePost(id, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
