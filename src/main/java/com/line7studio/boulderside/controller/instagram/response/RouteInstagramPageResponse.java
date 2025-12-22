package com.line7studio.boulderside.controller.instagram.response;

import java.util.List;

public record RouteInstagramPageResponse(
	List<RouteInstagramResponse> content,
	Long nextCursor,
	boolean hasNext,
	int size
) {
	public static RouteInstagramPageResponse of(List<RouteInstagramResponse> content, Long nextCursor, boolean hasNext, int size) {
		return new RouteInstagramPageResponse(content, nextCursor, hasNext, size);
	}
}