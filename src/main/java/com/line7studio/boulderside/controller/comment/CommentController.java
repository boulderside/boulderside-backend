package com.line7studio.boulderside.controller.comment;

import com.line7studio.boulderside.application.comment.CommentUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.comment.request.CreateCommentRequest;
import com.line7studio.boulderside.controller.comment.request.UpdateCommentRequest;
import com.line7studio.boulderside.controller.comment.response.CommentPageResponse;
import com.line7studio.boulderside.controller.comment.response.CommentResponse;
import com.line7studio.boulderside.controller.comment.response.MyCommentPageResponse;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentUseCase commentUseCase;

    @GetMapping("/{domainType}/{domainId}")
    public ResponseEntity<ApiResponse<CommentPageResponse>> getComments(
            @PathVariable String domainType,
            @PathVariable Long domainId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentPageResponse response = commentUseCase.getCommentPage(
                cursor, size, domainId, CommentDomainType.fromPath(domainType), userDetails.userId()
        );
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyCommentPageResponse>> getMyComments(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String domainType,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyCommentPageResponse response = commentUseCase.getMyComments(cursor, size, userDetails.userId(), domainType);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @PostMapping("/{domainType}/{domainId}")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable String domainType,
            @PathVariable Long domainId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentResponse comment = commentUseCase.createComment(
                domainId, CommentDomainType.fromPath(domainType), request, userDetails.userId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(comment));
    }

    @PutMapping("/{domainType}/{domainId}/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable String domainType,
            @PathVariable Long domainId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentResponse comment = commentUseCase.updateComment(commentId, request, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.of(comment));
    }

    @DeleteMapping("/{domainType}/{domainId}/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable String domainType,
            @PathVariable Long domainId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentUseCase.deleteComment(commentId, userDetails.userId());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
