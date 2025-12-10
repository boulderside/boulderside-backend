package com.line7studio.boulderside.controller.boardpost;

import com.line7studio.boulderside.application.post.BoardPostUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.boardpost.request.CreateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.request.UpdateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.response.BoardPostResponse;
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

import com.line7studio.boulderside.common.security.details.CustomUserDetails;

@RestController
@RequestMapping("/admin/board-posts")
@RequiredArgsConstructor
public class AdminBoardPostController {

    private final BoardPostUseCase boardPostUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<BoardPostResponse>> createBoardPost(
        @Valid @RequestBody CreateBoardPostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        BoardPostResponse response = boardPostUseCase.adminCreateBoardPost(request, userDetails.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<BoardPostResponse>> updateBoardPost(
        @PathVariable Long postId,
        @Valid @RequestBody UpdateBoardPostRequest request
    ) {
        BoardPostResponse response = boardPostUseCase.adminUpdateBoardPost(postId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoardPost(@PathVariable Long postId) {
        boardPostUseCase.adminDeleteBoardPost(postId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
