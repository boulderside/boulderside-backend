package com.line7studio.boulderside.controller.notice.response;

import java.util.List;

public record NoticePageResponse(
    List<NoticeResponse> content,
    int page,
    int size,
    long totalElements,
    boolean hasNext
) {

    public static NoticePageResponse of(List<NoticeResponse> content, int page, int size, long totalElements, boolean hasNext) {
        return new NoticePageResponse(content, page, size, totalElements, hasNext);
    }
}
