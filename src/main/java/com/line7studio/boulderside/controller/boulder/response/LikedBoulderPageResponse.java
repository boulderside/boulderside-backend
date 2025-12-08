package com.line7studio.boulderside.controller.boulder.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikedBoulderPageResponse {
	private final List<LikedBoulderItemResponse> content;
	private final Long nextCursor;
	private final boolean hasNext;
	private final int size;

	public static LikedBoulderPageResponse of(List<LikedBoulderItemResponse> content, Long nextCursor,
		boolean hasNext, int size) {
		return LikedBoulderPageResponse.builder()
			.content(content)
			.nextCursor(nextCursor)
			.hasNext(hasNext)
			.size(size)
			.build();
	}
}
