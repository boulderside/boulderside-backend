package com.line7studio.boulderside.controller.instagram.response;

import com.line7studio.boulderside.domain.instagram.Instagram;
import com.line7studio.boulderside.domain.instagram.RouteInstagram;

import java.time.LocalDateTime;
import java.util.List;

public record RouteInstagramResponse(
	Long routeInstagramId,
	Long routeId,
	Long instagramId,
	InstagramResponse instagram,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static RouteInstagramResponse of(RouteInstagram routeInstagram, Instagram instagram, List<Long> routeIds) {
		return new RouteInstagramResponse(
			routeInstagram.getId(),
			routeInstagram.getRouteId(),
			routeInstagram.getInstagramId(),
			InstagramResponse.of(instagram, routeIds),
			routeInstagram.getCreatedAt(),
			routeInstagram.getUpdatedAt()
		);
	}
}