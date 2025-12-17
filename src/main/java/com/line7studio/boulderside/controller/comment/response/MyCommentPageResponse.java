package com.line7studio.boulderside.controller.comment.response;

import java.util.List;

public record MyCommentPageResponse(
    List<MyCommentResponse> content,
    Long nextCursor,
    boolean hasNext,
    int size
) {
    public static MyCommentPageResponse of(List<MyCommentResponse> content, Long nextCursor, boolean hasNext, int size) {
        return new MyCommentPageResponse(content, nextCursor, hasNext, size);
    }
}