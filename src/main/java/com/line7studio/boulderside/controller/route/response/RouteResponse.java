package com.line7studio.boulderside.controller.route.response;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.route.entity.Route;

import java.time.LocalDateTime;
import java.util.List;

public record RouteResponse(
    Long routeId,
    BoulderInfo boulderInfo,
    String province,
    String city,
    String name,
    String pioneerName,
    Double latitude,
    Double longitude,
    String sectorName,
    String areaCode,
    Level routeLevel,
    Long likeCount,
    Boolean liked,
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

    public static RouteResponse of(Route route, String province, String city, String boulderName, String sectorName, String areaCode,
        List<ImageInfo> imageInfoList, Long likeCount, Boolean liked) {
        return new RouteResponse(
            route.getId(),
            BoulderInfo.of(route.getBoulderId(), boulderName),
            province,
            city,
            route.getName(),
            route.getPioneerName(),
            route.getLatitude(),
            route.getLongitude(),
            sectorName,
            areaCode,
            route.getRouteLevel(),
            likeCount,
            liked,
            route.getViewCount(),
            route.getClimberCount(),
            route.getCommentCount(),
            imageInfoList,
            route.getCreatedAt(),
            route.getUpdatedAt()
        );
    }
}