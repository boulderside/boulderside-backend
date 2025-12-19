package com.line7studio.boulderside.controller.completion.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.completion.Completion;
import com.line7studio.boulderside.domain.route.Route;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CompletionResponse(
	Long completionId,
	Long routeId,
	Long userId,
	String routeName,
	Level routeLevel,
	String boulderName,
	LocalDate completedDate,
	String memo,
	boolean completed,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static CompletionResponse from(Completion completion, Route route, Boulder boulder) {
		return new CompletionResponse(
			completion.getId(),
			completion.getRouteId(),
			completion.getUserId(),
			route.getName(),
			route.getRouteLevel(),
			boulder.getName(),
			completion.getCompletedDate(),
			completion.getMemo(),
			true,
			completion.getCreatedAt(),
			completion.getUpdatedAt()
		);
	}
}
