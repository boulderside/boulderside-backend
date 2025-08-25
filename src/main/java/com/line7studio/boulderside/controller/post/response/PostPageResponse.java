package com.line7studio.boulderside.controller.post.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPageResponse {
	private List<PostResponse> content;
	private Long nextCursor;
	private boolean hasNext;
	private int size;

	public static PostPageResponse of(List<PostResponse> content, Long nextCursor, boolean hasNext, int size) {
		return PostPageResponse.builder()
			.content(content)
			.nextCursor(nextCursor)
			.hasNext(hasNext)
			.size(size)
			.build();
	}
}