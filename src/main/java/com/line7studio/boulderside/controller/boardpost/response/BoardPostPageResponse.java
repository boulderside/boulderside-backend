package com.line7studio.boulderside.controller.boardpost.response;

import java.util.List;

public record BoardPostPageResponse(
    List<BoardPostResponse> content,
    Long nextCursor,
    String nextSubCursor,
    boolean hasNext,
    int size
) {
    public static BoardPostPageResponse of(List<BoardPostResponse> content, Long nextCursor, String nextSubCursor, boolean hasNext, int size) {
        return new BoardPostPageResponse(content, nextCursor, nextSubCursor, hasNext, size);
    }
}