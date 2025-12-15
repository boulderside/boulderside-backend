package com.line7studio.boulderside.controller.comment.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.feature.comment.entity.Comment;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private CommentDomainType commentDomainType;
    private Long domainId;
    private Boolean isMine;
    private UserInfo userInfo;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long commentCount;

    public static CommentResponse of(Comment comment, UserInfo userInfo, Boolean isMine, Long commentCount) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .commentDomainType(comment.getCommentDomainType())
                .domainId(comment.getDomainId())
                .isMine(isMine)
                .userInfo(userInfo)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .commentCount(commentCount)
                .build();
    }

    public static CommentResponse of(Comment comment, UserInfo userInfo, Boolean isMine) {
        return of(comment, userInfo, isMine, null);
    }
}