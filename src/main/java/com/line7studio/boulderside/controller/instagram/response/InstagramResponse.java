package com.line7studio.boulderside.controller.instagram.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.instagram.Instagram;

import java.time.LocalDateTime;
import java.util.List;

public record InstagramResponse(
	Long instagramId,
	String url,
	UserInfo userInfo,
	List<RouteInfo> routes,
	Long likeCount,
	Boolean isLiked,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public record RouteInfo(
		Long routeId,
		String name,
		String boulderName
	) {
		public static RouteInfo of(Long routeId, String routeName, String boulderName) {
			return new RouteInfo(routeId, routeName, boulderName);
		}
	}

	public static InstagramResponse of(Instagram instagram, UserInfo userInfo, List<RouteInfo> routes) {
		return new InstagramResponse(
			instagram.getId(),
			instagram.getUrl(),
			userInfo,
			routes,
			instagram.getLikeCount(),
			null,
			instagram.getCreatedAt(),
			instagram.getUpdatedAt()
		);
	}

	public static InstagramResponse of(Instagram instagram, UserInfo userInfo, List<RouteInfo> routes, Boolean isLiked) {
		return new InstagramResponse(
			instagram.getId(),
			instagram.getUrl(),
			userInfo,
			routes,
			instagram.getLikeCount(),
			isLiked,
			instagram.getCreatedAt(),
			instagram.getUpdatedAt()
		);
	}
}