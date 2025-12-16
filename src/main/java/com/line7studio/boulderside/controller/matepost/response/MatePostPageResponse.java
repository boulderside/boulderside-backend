package com.line7studio.boulderside.controller.matepost.response;

import java.util.List;

public record MatePostPageResponse(
    List<MatePostResponse> content,
    Long nextCursor,
    String nextSubCursor,
    boolean hasNext,
    int size
) {
    public static MatePostPageResponse of(List<MatePostResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
        return new MatePostPageResponse(content, nextCursor, nextSubCursor, hasNext, size);
    }
}