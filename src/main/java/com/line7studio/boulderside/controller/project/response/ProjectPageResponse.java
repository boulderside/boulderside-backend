package com.line7studio.boulderside.controller.project.response;

import java.util.List;

public record ProjectPageResponse(
    List<ProjectResponse> content,
    Long nextCursor,
    boolean hasNext,
    int size
) {
    public static ProjectPageResponse of(List<ProjectResponse> content, Long nextCursor, boolean hasNext, int size) {
        return new ProjectPageResponse(content, nextCursor, hasNext, size);
    }
}