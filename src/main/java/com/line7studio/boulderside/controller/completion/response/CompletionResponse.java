package com.line7studio.boulderside.controller.completion.response;

import com.line7studio.boulderside.domain.completion.Completion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CompletionResponse(
	Long completionId,
	Long routeId,
	Long userId,
	LocalDate completedDate,
	String memo,
	boolean completed,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static CompletionResponse from(Completion completion) {
		return new CompletionResponse(
			completion.getId(),
			completion.getRouteId(),
			completion.getUserId(),
			completion.getCompletedDate(),
			completion.getMemo(),
			true,
			completion.getCreatedAt(),
			completion.getUpdatedAt()
		);
	}
}
