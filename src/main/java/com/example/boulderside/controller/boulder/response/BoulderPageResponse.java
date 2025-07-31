package com.example.boulderside.controller.boulder.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoulderPageResponse {
	private List<BoulderResponse> content;
	private Long nextCursor;
	private boolean hasNext;
	private int size;

	public static BoulderPageResponse of(List<BoulderResponse> content, Long nextCursor, boolean hasNext, int size) {
		return BoulderPageResponse.builder()
			.content(content)
			.nextCursor(nextCursor)
			.hasNext(hasNext)
			.size(size)
			.build();
	}
}