package com.line7studio.boulderside.controller.route.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutePageResponse {
	private List<RouteResponse> content;
	private Long nextCursor;
	private String nextSubCursor;
	private boolean hasNext;
	private int size;

	public static RoutePageResponse of(List<RouteResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
		return RoutePageResponse.builder()
			.content(content)
			.nextCursor(nextCursor)
			.nextSubCursor(nextSubCursor)
			.hasNext(hasNext)
			.size(size)
			.build();
	}
}