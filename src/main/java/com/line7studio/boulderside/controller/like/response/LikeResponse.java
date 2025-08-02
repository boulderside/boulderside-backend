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
	private long boulderId;
	private boolean isLiked;
	private long likeCount;

	public static LikeResponse of(long boulderId, boolean isLiked, long likeCount) {
		return LikeResponse.builder()
			.boulderId(boulderId)
			.isLiked(isLiked)
			.likeCount(likeCount)
			.build();
	}
}