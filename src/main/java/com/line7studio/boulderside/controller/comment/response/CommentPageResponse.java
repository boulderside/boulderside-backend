package com.line7studio.boulderside.controller.comment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageResponse {
    private List<CommentResponse> content;
    private Long nextCursor;
    private boolean hasNext;
    private int size;

    public static CommentPageResponse of(List<CommentResponse> content, Long nextCursor, boolean hasNext, int size) {
        return CommentPageResponse.builder()
                .content(content)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .size(size)
                .build();
    }
}