package com.line7studio.boulderside.controller.route.response;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.route.Route;

import java.time.LocalDateTime;
import java.util.List;

public record RouteResponse(
    Long routeId,
    BoulderInfo boulderInfo,
    String province,
    String city,
    String name,
    String pioneerName,
    Level routeLevel,
    Long likeCount,
    Boolean liked,
    Boolean completed,
    Long viewCount,
    Long climberCount,
    Long commentCount,
    List<ImageInfo> imageInfoList,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record BoulderInfo(
        Long boulderId,
        String name
    ) {
        public static BoulderInfo of(Long boulderId, String name) {
            return new BoulderInfo(boulderId, name);
        }
    }

    public static RouteResponse of(Route route, String province, String city, String boulderName,
        List<ImageInfo> imageInfoList, Long likeCount, Boolean liked, Boolean completed) {
        return new RouteResponse(
            route.getId(),
            BoulderInfo.of(route.getBoulderId(), boulderName),
            province,
            city,
            route.getName(),
            route.getPioneerName(),
            route.getRouteLevel(),
            likeCount,
            liked,
            completed,
            route.getViewCount(),
            route.getClimberCount(),
            route.getCommentCount(),
            imageInfoList,
            route.getCreatedAt(),
            route.getUpdatedAt()
        );
    }
}
