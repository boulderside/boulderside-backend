package com.line7studio.boulderside.controller.instagram.response;

import com.line7studio.boulderside.domain.instagram.Instagram;
import com.line7studio.boulderside.domain.instagram.interaction.like.UserInstagramLike;

import java.time.LocalDateTime;
import java.util.List;

public record LikedInstagramItemResponse(
    Long likeId,
    Long instagramId,
    String url,
    List<RouteInfo> routes,
    Long likeCount,
    Boolean liked,
    LocalDateTime likedAt
) {
    public record RouteInfo(
        Long routeId,
        String name,
        String boulderName
    ) {
        public static RouteInfo of(Long routeId, String routeName, String boulderName) {
            return new RouteInfo(routeId, routeName, boulderName);
        }
    }

    public static LikedInstagramItemResponse of(UserInstagramLike like, Instagram instagram, List<RouteInfo> routes) {
        return new LikedInstagramItemResponse(
            like.getId(),
            instagram.getId(),
            instagram.getUrl(),
            routes,
            instagram.getLikeCount(),
            true,
            like.getCreatedAt()
        );
    }
}