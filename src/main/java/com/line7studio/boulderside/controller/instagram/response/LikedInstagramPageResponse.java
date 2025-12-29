package com.line7studio.boulderside.controller.instagram.response;

import java.util.List;

public record LikedInstagramPageResponse(
    List<LikedInstagramItemResponse> content,
    Long nextCursor,
    boolean hasNext,
    int size
) {
    public static LikedInstagramPageResponse of(List<LikedInstagramItemResponse> content, Long nextCursor,
        boolean hasNext, int size) {
        return new LikedInstagramPageResponse(content, nextCursor, hasNext, size);
    }
}