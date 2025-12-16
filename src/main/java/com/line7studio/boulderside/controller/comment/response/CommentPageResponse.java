package com.line7studio.boulderside.controller.comment.response;

import java.util.List;

public record CommentPageResponse(
    List<CommentResponse> content,
    Long nextCursor,
    boolean hasNext,
    int size
) {
    public static CommentPageResponse of(List<CommentResponse> content, Long nextCursor, boolean hasNext, int size) {
        return new CommentPageResponse(content, nextCursor, hasNext, size);
    }
}