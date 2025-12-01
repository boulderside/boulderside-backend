package com.line7studio.boulderside.controller.matepost.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatePostPageResponse {
    private List<MatePostResponse> content;
    private Long nextCursor;
    private String nextSubCursor;
    private boolean hasNext;
    private int size;

    public static MatePostPageResponse of(List<MatePostResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
        return MatePostPageResponse.builder()
            .content(content)
            .nextCursor(nextCursor)
            .nextSubCursor(nextSubCursor)
            .hasNext(hasNext)
            .size(size)
            .build();
    }
}
