package com.line7studio.boulderside.controller.boulder.response;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.domain.boulder.Boulder;

import java.time.LocalDateTime;
import java.util.List;

public record BoulderResponse(
    Long boulderId,
    String name,
    String description,
    Double latitude,
    Double longitude,
    String province,
    String city,
    Long likeCount,
    Long viewCount,
    Boolean liked,
    List<ImageInfo> imageInfoList,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static BoulderResponse of(Boulder boulder, String province, String city,
        List<ImageInfo> imageInfoList, Long likeCount, boolean isLiked) {
        return new BoulderResponse(
            boulder.getId(),
            boulder.getName(),
            boulder.getDescription(),
            boulder.getLatitude(),
            boulder.getLongitude(),
            province,
            city,
            likeCount,
            boulder.getViewCount(),
            isLiked,
            imageInfoList,
            boulder.getCreatedAt(),
            boulder.getUpdatedAt()
        );
    }
}