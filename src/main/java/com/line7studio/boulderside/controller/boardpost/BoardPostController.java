package com.line7studio.boulderside.controller.boardpost;

import com.line7studio.boulderside.usecase.post.BoardPostUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.boardpost.request.CreateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.request.UpdateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.response.BoardPostPageResponse;
import com.line7studio.boulderside.controller.boardpost.response.BoardPostResponse;
import com.line7studio.boulderside.domain.feature.post.enums.BoardPostSortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board-posts")
@RequiredArgsConstructor
@Slf4j
public class BoardPostController {

    private final BoardPostUseCase boardPostUseCase;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<BoardPostPageResponse>> getBoardPostPage(
        @RequestParam(required = false) Long cursor,
        @RequestParam(required = false) String subCursor,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "LATEST_CREATED") BoardPostSortType postSortType,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardPostPageResponse response = boardPostUseCase.getBoardPostPage(cursor, subCursor, size, postSortType, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<BoardPostPageResponse>> getMyBoardPosts(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "10") int size,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardPostPageResponse response = boardPostUseCase.getMyBoardPosts(cursor, size, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardPostResponse>> getBoardPost(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardPostResponse response = boardPostUseCase.getBoardPost(id, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BoardPostResponse>> createBoardPost(
        @Valid @RequestBody CreateBoardPostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardPostResponse response = boardPostUseCase.createBoardPost(request, userDetails.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardPostResponse>> updateBoardPost(
        @PathVariable Long id,
        @Valid @RequestBody UpdateBoardPostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BoardPostResponse response = boardPostUseCase.updateBoardPost(id, request, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoardPost(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boardPostUseCase.deleteBoardPost(id, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
