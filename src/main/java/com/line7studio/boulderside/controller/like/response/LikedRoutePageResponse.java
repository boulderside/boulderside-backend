package com.line7studio.boulderside.controller.like.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikedRoutePageResponse {
	private final List<LikedRouteItemResponse> content;
	private final Long nextCursor;
	private final boolean hasNext;
	private final int size;

	public static LikedRoutePageResponse of(List<LikedRouteItemResponse> content, Long nextCursor,
		boolean hasNext, int size) {
		return LikedRoutePageResponse.builder()
			.content(content)
			.nextCursor(nextCursor)
			.hasNext(hasNext)
			.size(size)
			.build();
	}
}
