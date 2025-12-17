package com.line7studio.boulderside.controller.matepost.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.post.entity.MatePost;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MatePostResponse(
    Long matePostId,
    Boolean isMine,
    UserInfo userInfo,
    String title,
    String content,
    Long viewCount,
    Long commentCount,
    LocalDate meetingDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static MatePostResponse of(MatePost matePost, UserInfo userInfo, Boolean isMine, Long commentCount) {
        return new MatePostResponse(
            matePost.getId(),
            isMine,
            userInfo,
            matePost.getTitle(),
            matePost.getContent(),
            matePost.getViewCount(),
            commentCount,
            matePost.getMeetingDate(),
            matePost.getCreatedAt(),
            matePost.getUpdatedAt()
        );
    }
}