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
	private String nextSubCursor;
	private boolean hasNext;
	private int size;

	public static PostPageResponse of(List<PostResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
		return PostPageResponse.builder()
			.content(content)
			.nextCursor(nextCursor)
			.nextSubCursor(nextSubCursor)
			.hasNext(hasNext)
			.size(size)
			.build();
	}
}