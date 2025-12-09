package com.line7studio.boulderside.controller.route.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteLikeResponse {
	private final Long routeId;
	private final boolean liked;
	private final long likeCount;

	public static RouteLikeResponse of(Long routeId, boolean liked, long likeCount) {
		return RouteLikeResponse.builder()
			.routeId(routeId)
			.liked(liked)
			.likeCount(likeCount)
			.build();
	}
}
