package com.line7studio.boulderside.controller.boulder.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoulderLikeResponse {
	private final Long boulderId;
	private final boolean liked;
	private final long likeCount;

	public static BoulderLikeResponse of(Long boulderId, boolean liked, long likeCount) {
		return BoulderLikeResponse.builder()
			.boulderId(boulderId)
			.liked(liked)
			.likeCount(likeCount)
			.build();
	}
}
