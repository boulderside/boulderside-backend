package com.line7studio.boulderside.controller.completion.response;

import java.util.List;

public record CompletionPageResponse(
	List<CompletionResponse> content,
	Long nextCursor,
	boolean hasNext,
	int size
) {
	public static CompletionPageResponse of(List<CompletionResponse> content, Long nextCursor, boolean hasNext, int size) {
		return new CompletionPageResponse(content, nextCursor, hasNext, size);
	}
}
