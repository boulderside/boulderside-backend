package com.line7studio.boulderside.controller.boulder.response;

import java.time.LocalDateTime;

import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikedBoulderItemResponse {
	private final Long likeId;
	private final Long boulderId;
	private final String name;
	private final Double latitude;
	private final Double longitude;
	private final Long regionId;
	private final Long sectorId;
	private final Long likeCount;
	private final Long viewCount;
	private final LocalDateTime likedAt;

	public static LikedBoulderItemResponse of(UserBoulderLike like, Boulder boulder) {
		return LikedBoulderItemResponse.builder()
			.likeId(like.getId())
			.boulderId(boulder.getId())
			.name(boulder.getName())
			.latitude(boulder.getLatitude())
			.longitude(boulder.getLongitude())
			.regionId(boulder.getRegionId())
			.sectorId(boulder.getSectorId())
			.likeCount(boulder.getLikeCount())
			.viewCount(boulder.getViewCount())
			.likedAt(like.getCreatedAt())
			.build();
	}
}
