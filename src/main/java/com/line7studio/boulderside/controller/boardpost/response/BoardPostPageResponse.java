package com.line7studio.boulderside.controller.boardpost.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostPageResponse {
    private List<BoardPostResponse> content;
    private Long nextCursor;
    private String nextSubCursor;
    private boolean hasNext;
    private int size;

    public static BoardPostPageResponse of(List<BoardPostResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
        return BoardPostPageResponse.builder()
            .content(content)
            .nextCursor(nextCursor)
            .nextSubCursor(nextSubCursor)
            .hasNext(hasNext)
            .size(size)
            .build();
    }
}
