package com.line7studio.boulderside.controller.route.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LikedRoutePageResponse(List<LikedRouteItemResponse> content, Long nextCursor, boolean hasNext, int size) {
    public static LikedRoutePageResponse of(List<LikedRouteItemResponse> content, Long nextCursor,
                                            boolean hasNext, int size) {
        return LikedRoutePageResponse.builder()
                .content(content)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .size(size)
                .build();
    }
}
