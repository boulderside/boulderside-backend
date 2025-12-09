package com.line7studio.boulderside.controller.route.response;

import java.time.LocalDateTime;

import com.line7studio.boulderside.domain.feature.route.interaction.completion.entity.UserRouteCompletion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteCompletionResponse {
	private final Long routeId;
	private final Long userId;
	private final Boolean completed;
	private final String memo;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public static RouteCompletionResponse from(UserRouteCompletion completion) {
		return RouteCompletionResponse.builder()
			.routeId(completion.getRouteId())
			.userId(completion.getUserId())
			.completed(completion.getCompleted())
			.memo(completion.getMemo())
			.createdAt(completion.getCreatedAt())
			.updatedAt(completion.getUpdatedAt())
			.build();
	}
}
