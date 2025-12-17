package com.line7studio.boulderside.controller.boulder.response;

import java.util.List;

public record BoulderPageResponse(
    List<BoulderResponse> content,
    Long nextCursor,
    String nextSubCursor,
    boolean hasNext,
    int size
) {
    public static BoulderPageResponse of(List<BoulderResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
        return new BoulderPageResponse(content, nextCursor, nextSubCursor, hasNext, size);
    }
}