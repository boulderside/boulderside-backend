package com.line7studio.boulderside.controller.comment.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.feature.comment.Comment;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;

import java.time.LocalDateTime;

public record MyCommentResponse(
    Long commentId,
    CommentDomainType commentDomainType,
    Long domainId,
    String content,
    String domainTitle,
    Boolean isMine,
    UserInfo userInfo,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static MyCommentResponse of(Comment comment, UserInfo userInfo, Boolean isMine, String domainTitle) {
        return new MyCommentResponse(
            comment.getId(),
            comment.getCommentDomainType(),
            comment.getDomainId(),
            comment.getContent(),
            domainTitle,
            isMine,
            userInfo,
            comment.getCreatedAt(),
            comment.getUpdatedAt()
        );
    }
}