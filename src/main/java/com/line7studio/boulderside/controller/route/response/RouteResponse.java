package com.line7studio.boulderside.controller.route.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
	private Long routeId;
	private Long boulderId;
	private String name;
	private Level routeLevel;
	private Long likeCount;
	private Boolean liked;
	private Long viewCount;
	private Long climberCount;
	private Long commentCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static RouteResponse of(Route route, Long likeCount, Boolean liked) {
		return RouteResponse.builder()
			.routeId(route.getId())
			.boulderId(route.getBoulderId())
			.name(route.getName())
			.routeLevel(route.getRouteLevel())
			.likeCount(likeCount)
			.liked(liked)
			.viewCount(route.getViewCount())
			.climberCount(route.getClimberCount())
			.commentCount(route.getCommentCount())
			.createdAt(route.getCreatedAt())
			.updatedAt(route.getUpdatedAt())
			.build();
	}
}