package com.line7studio.boulderside.controller.notice.response;

import com.line7studio.boulderside.domain.notice.Notice;
import java.time.LocalDateTime;

public record NoticeResponse(
    Long id,
    String title,
    String content,
    boolean pinned,
    long viewCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            notice.isPinned(),
            notice.getViewCount(),
            notice.getCreatedAt(),
            notice.getUpdatedAt()
        );
    }
}
