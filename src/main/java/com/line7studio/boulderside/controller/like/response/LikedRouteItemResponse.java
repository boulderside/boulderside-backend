package com.line7studio.boulderside.controller.like.response;

import java.time.LocalDateTime;

import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.route.interaction.like.entity.UserRouteLike;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikedRouteItemResponse {
	private final Long likeId;
	private final Long routeId;
	private final String name;
	private final Long boulderId;
	private final Long regionId;
	private final Long sectorId;
	private final Long likeCount;
	private final Long viewCount;
	private final LocalDateTime likedAt;

	public static LikedRouteItemResponse of(UserRouteLike like, Route route) {
		return LikedRouteItemResponse.builder()
			.likeId(like.getId())
			.routeId(route.getId())
			.name(route.getName())
			.boulderId(route.getBoulderId())
			.regionId(route.getRegionId())
			.sectorId(route.getSectorId())
			.likeCount(route.getLikeCount())
			.viewCount(route.getViewCount())
			.likedAt(like.getCreatedAt())
			.build();
	}
}
