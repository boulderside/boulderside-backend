package com.line7studio.boulderside.controller.boardpost.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;

import java.time.LocalDateTime;

public record BoardPostResponse(
    Long boardPostId,
    Boolean isMine,
    UserInfo userInfo,
    String title,
    String content,
    Long viewCount,
    Long commentCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long userId
) {
    public static BoardPostResponse of(BoardPost boardPost, UserInfo userInfo, Boolean isMine, Long commentCount) {
        return new BoardPostResponse(
            boardPost.getId(),
            isMine,
            userInfo,
            boardPost.getTitle(),
            boardPost.getContent(),
            boardPost.getViewCount(),
            commentCount,
            boardPost.getCreatedAt(),
            boardPost.getUpdatedAt(),
            userInfo.getId()
        );
    }
}