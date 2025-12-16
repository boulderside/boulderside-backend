package com.line7studio.boulderside.controller.boulder.response;

import java.util.List;

public record LikedBoulderPageResponse(
    List<LikedBoulderItemResponse> content,
    Long nextCursor,
    boolean hasNext,
    int size
) {
    public static LikedBoulderPageResponse of(List<LikedBoulderItemResponse> content, Long nextCursor,
        boolean hasNext, int size) {
        return new LikedBoulderPageResponse(content, nextCursor, hasNext, size);
    }
}