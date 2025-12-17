package com.line7studio.boulderside.controller.comment.response;

public record CommentCountResponse(
    Integer commentCount
) {
    public static CommentCountResponse of(Integer commentCount) {
        return new CommentCountResponse(commentCount);
    }
}