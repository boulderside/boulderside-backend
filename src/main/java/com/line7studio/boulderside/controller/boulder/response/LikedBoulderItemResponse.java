package com.line7studio.boulderside.controller.boulder.response;

import com.line7studio.boulderside.domain.feature.boulder.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;

import java.time.LocalDateTime;

public record LikedBoulderItemResponse(
    Long likeId,
    Long boulderId,
    String name,
    String province,
    String city,
    Long likeCount,
    Long viewCount,
    Boolean liked,
    LocalDateTime likedAt
) {
    public static LikedBoulderItemResponse of(UserBoulderLike like, Boulder boulder, String province, String city) {
        return new LikedBoulderItemResponse(
            like.getId(),
            boulder.getId(),
            boulder.getName(),
            province,
            city,
            boulder.getLikeCount(),
            boulder.getViewCount(),
            true, // 좋아요 목록이므로 항상 true
            like.getCreatedAt()
        );
    }
}