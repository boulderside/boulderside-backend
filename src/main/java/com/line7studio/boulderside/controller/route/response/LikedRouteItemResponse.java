package com.line7studio.boulderside.controller.route.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.route.interaction.like.entity.UserRouteLike;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LikedRouteItemResponse(Long likeId, Long routeId, String name, BoulderInfo boulderInfo, Level routeLevel,
                                     Long likeCount, Long viewCount, Long climberCount, LocalDateTime likedAt) {
    @Builder
    public record BoulderInfo(
            Long boulderId,
            String name
    ) {
        public static BoulderInfo of(Long boulderId, String name) {
            return BoulderInfo.builder()
                    .boulderId(boulderId)
                    .name(name)
                    .build();
        }
    }

    public static LikedRouteItemResponse of(UserRouteLike like, Route route, String boulderName) {
        return LikedRouteItemResponse.builder()
                .likeId(like.getId())
                .routeId(route.getId())
                .name(route.getName())
                .boulderInfo(BoulderInfo.of(route.getBoulderId(), boulderName))
                .routeLevel(route.getRouteLevel())
                .likeCount(route.getLikeCount())
                .viewCount(route.getViewCount())
                .climberCount(route.getClimberCount())
                .likedAt(like.getCreatedAt())
                .build();
    }
}
