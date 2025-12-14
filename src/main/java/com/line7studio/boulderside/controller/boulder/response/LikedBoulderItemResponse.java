package com.line7studio.boulderside.controller.boulder.response;

import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LikedBoulderItemResponse {
	private final Long likeId;
	private final Long boulderId;
	private final String name;
	private final String province;
	private final String city;
	private final Long likeCount;
	private final Long viewCount;
	private final Boolean liked;
	private final LocalDateTime likedAt;

	public static LikedBoulderItemResponse of(UserBoulderLike like, Boulder boulder, String province, String city) {
		return LikedBoulderItemResponse.builder()
			.likeId(like.getId())
			.boulderId(boulder.getId())
			.name(boulder.getName())
			.province(province)
			.city(city)
			.likeCount(boulder.getLikeCount())
			.viewCount(boulder.getViewCount())
			.liked(true) // 좋아요 목록이므로 항상 true
			.likedAt(like.getCreatedAt())
			.build();
	}
}
