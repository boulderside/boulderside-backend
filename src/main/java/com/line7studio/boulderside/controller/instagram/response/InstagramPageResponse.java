package com.line7studio.boulderside.controller.instagram.response;

import java.util.List;

public record InstagramPageResponse(
	List<InstagramResponse> content,
	Long nextCursor,
	boolean hasNext,
	int size
) {
	public static InstagramPageResponse of(List<InstagramResponse> content, Long nextCursor, boolean hasNext, int size) {
		return new InstagramPageResponse(content, nextCursor, hasNext, size);
	}
}