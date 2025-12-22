package com.line7studio.boulderside.controller.instagram.response;

import com.line7studio.boulderside.domain.instagram.Instagram;

import java.time.LocalDateTime;
import java.util.List;

public record InstagramResponse(
	Long instagramId,
	String url,
	Long userId,
	List<Long> routeIds,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static InstagramResponse of(Instagram instagram, List<Long> routeIds) {
		return new InstagramResponse(
			instagram.getId(),
			instagram.getUrl(),
			instagram.getUserId(),
			routeIds,
			instagram.getCreatedAt(),
			instagram.getUpdatedAt()
		);
	}
}