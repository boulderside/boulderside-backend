package com.line7studio.boulderside.controller.boardpost;

import com.line7studio.boulderside.usecase.post.BoardPostUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.boardpost.request.CreateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.request.UpdateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.response.BoardPostPageResponse;
import com.line7studio.boulderside.controller.boardpost.response.BoardPostResponse;
import com.line7studio.boulderside.controller.common.request.UpdatePostStatusRequest;
import com.line7studio.boulderside.domain.board.enums.BoardPostSortType;
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

import com.line7studio.boulderside.common.security.details.CustomUserDetails;

@RestController
@RequestMapping("/admin/board-posts")
@RequiredArgsConstructor
public class AdminBoardPostController {

    private final BoardPostUseCase boardPostUseCase;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<BoardPostPageResponse>> getBoardPostPage(
        @RequestParam(required = false) Long cursor,
        @RequestParam(required = false) String subCursor,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "LATEST_CREATED") BoardPostSortType postSortType,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardPostPageResponse response = boardPostUseCase.getBoardPostPageForAdmin(cursor, subCursor, size, postSortType, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<BoardPostResponse>> getBoardPost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardPostResponse response = boardPostUseCase.getBoardPostForAdmin(postId, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

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

    @PatchMapping("/{postId}/status")
    public ResponseEntity<ApiResponse<Void>> updateBoardPostStatus(
        @PathVariable Long postId,
        @Valid @RequestBody UpdatePostStatusRequest request
    ) {
        boardPostUseCase.updateBoardPostStatus(postId, request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
