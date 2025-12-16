package com.line7studio.boulderside.controller.route.response;

import java.util.List;

public record RoutePageResponse(
    List<RouteResponse> content,
    Long nextCursor,
    String nextSubCursor,
    boolean hasNext,
    int size
) {
    public static RoutePageResponse of(List<RouteResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
        return new RoutePageResponse(content, nextCursor, nextSubCursor, hasNext, size);
    }
}