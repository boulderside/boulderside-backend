package com.line7studio.boulderside.controller.comment;

import com.line7studio.boulderside.usecase.comment.CommentUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.comment.request.CreateAdminCommentRequest;
import com.line7studio.boulderside.controller.comment.request.UpdateCommentRequest;
import com.line7studio.boulderside.controller.comment.response.CommentResponse;
import com.line7studio.boulderside.controller.comment.response.CommentPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;

import com.line7studio.boulderside.controller.comment.response.CommentCountResponse;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentUseCase commentUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<CommentPageResponse>> getComments(
        @RequestParam String domainType,
        @RequestParam Long domainId,
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "20") int size,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CommentPageResponse response = commentUseCase.getCommentPage(
            cursor, size, domainId, CommentDomainType.fromPath(domainType), userDetails.userId()
        );
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
        @Valid @RequestBody CreateAdminCommentRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CommentResponse response = commentUseCase.adminCreateComment(request, userDetails.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody UpdateCommentRequest request
    ) {
        CommentResponse response = commentUseCase.adminUpdateComment(commentId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentCountResponse>> deleteComment(@PathVariable Long commentId) {
        Integer count = commentUseCase.adminDeleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.of(CommentCountResponse.of(count)));
    }
}
