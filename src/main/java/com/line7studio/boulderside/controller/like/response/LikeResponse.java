package com.line7studio.boulderside.controller.like.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
	private long targetId;
	private String targetType;
	private boolean liked;
	private long likeCount;

	public static LikeResponse of(long targetId, String targetType, boolean liked, long likeCount) {
		return LikeResponse.builder()
			.targetId(targetId)
			.targetType(targetType)
			.liked(liked)
			.likeCount(likeCount)
			.build();
	}
}
