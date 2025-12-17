package com.line7studio.boulderside.controller.boulder.response;

import com.line7studio.boulderside.usecase.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.domain.feature.boulder.Boulder;

import java.time.LocalDateTime;
import java.util.List;

public record BoulderResponse(
    Long boulderId,
    String name,
    String description,
    Double latitude,
    Double longitude,
    String sectorName,
    String areaCode,
    String province,
    String city,
    Long likeCount,
    Long viewCount,
    Boolean liked,
    List<ImageInfo> imageInfoList,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static BoulderResponse of(Boulder boulder, String province, String city, String sectorName, String areaCode,
        List<ImageInfo> imageInfoList, Long likeCount, boolean isLiked) {
        return new BoulderResponse(
            boulder.getId(),
            boulder.getName(),
            boulder.getDescription(),
            boulder.getLatitude(),
            boulder.getLongitude(),
            sectorName,
            areaCode,
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

    public static BoulderResponse of(BoulderWithRegion boulderWithRegion, List<ImageInfo> imageInfoList,
        Long likeCount, Boolean isLiked) {
        return new BoulderResponse(
            boulderWithRegion.getId(),
            boulderWithRegion.getName(),
            boulderWithRegion.getDescription(),
            boulderWithRegion.getLatitude(),
            boulderWithRegion.getLongitude(),
            boulderWithRegion.getSectorName(),
            boulderWithRegion.getAreaCode(),
            boulderWithRegion.getProvince(),
            boulderWithRegion.getCity(),
            likeCount,
            boulderWithRegion.getViewCount(),
            isLiked,
            imageInfoList,
            boulderWithRegion.getCreatedAt(),
            boulderWithRegion.getUpdatedAt()
        );
    }
}