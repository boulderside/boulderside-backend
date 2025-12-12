package com.line7studio.boulderside.controller.project.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectPageResponse {
	private final List<ProjectResponse> content;
	private final Long nextCursor;
	private final boolean hasNext;
	private final int size;

	public static ProjectPageResponse of(List<ProjectResponse> content, Long nextCursor, boolean hasNext, int size) {
		return ProjectPageResponse.builder()
			.content(content)
			.nextCursor(nextCursor)
			.hasNext(hasNext)
			.size(size)
			.build();
	}
}
