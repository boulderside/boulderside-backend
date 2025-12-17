package com.line7studio.boulderside.controller.route.response;

import java.util.List;

public record LikedRoutePageResponse(
    List<LikedRouteItemResponse> content,
    Long nextCursor,
    boolean hasNext,
    int size
) {
    public static LikedRoutePageResponse of(List<LikedRouteItemResponse> content, Long nextCursor,
        boolean hasNext, int size) {
        return new LikedRoutePageResponse(content, nextCursor, hasNext, size);
    }
}