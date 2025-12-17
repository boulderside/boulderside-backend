package com.line7studio.boulderside.controller.route.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.interaction.like.entity.UserRouteLike;

import java.time.LocalDateTime;

public record LikedRouteItemResponse(
    Long likeId,
    Long routeId,
    String name,
    BoulderInfo boulderInfo,
    Level routeLevel,
    Long likeCount,
    Long viewCount,
    Long climberCount,
    LocalDateTime likedAt
) {
    public record BoulderInfo(
        Long boulderId,
        String name
    ) {
        public static BoulderInfo of(Long boulderId, String name) {
            return new BoulderInfo(boulderId, name);
        }
    }

    public static LikedRouteItemResponse of(UserRouteLike like, Route route, String boulderName) {
        return new LikedRouteItemResponse(
            like.getId(),
            route.getId(),
            route.getName(),
            BoulderInfo.of(route.getBoulderId(), boulderName),
            route.getRouteLevel(),
            route.getLikeCount(),
            route.getViewCount(),
            route.getClimberCount(),
            like.getCreatedAt()
        );
    }
}