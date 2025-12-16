package com.line7studio.boulderside.controller.route.response;

public record RouteLikeResponse(
    Long routeId,
    boolean liked,
    long likeCount
) {
    public static RouteLikeResponse of(Long routeId, boolean liked, long likeCount) {
        return new RouteLikeResponse(routeId, liked, likeCount);
    }
}