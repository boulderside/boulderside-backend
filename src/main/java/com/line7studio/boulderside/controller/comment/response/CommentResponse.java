package com.line7studio.boulderside.controller.comment.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.feature.comment.Comment;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;

import java.time.LocalDateTime;

public record CommentResponse(
    Long commentId,
    CommentDomainType commentDomainType,
    Long domainId,
    Boolean isMine,
    UserInfo userInfo,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long commentCount
) {
    public static CommentResponse of(Comment comment, UserInfo userInfo, Boolean isMine, Long commentCount) {
        return new CommentResponse(
            comment.getId(),
            comment.getCommentDomainType(),
            comment.getDomainId(),
            isMine,
            userInfo,
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getUpdatedAt(),
            commentCount
        );
    }

    public static CommentResponse of(Comment comment, UserInfo userInfo, Boolean isMine) {
        return of(comment, userInfo, isMine, null);
    }
}