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
public class MyCommentResponse {
    private Long commentId;
    private CommentDomainType commentDomainType;
    private Long domainId;
    private String content;
    private String domainTitle;
    private Boolean isMine;
    private UserInfo userInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MyCommentResponse of(Comment comment, UserInfo userInfo, Boolean isMine, String domainTitle) {
        return MyCommentResponse.builder()
                .commentId(comment.getId())
                .commentDomainType(comment.getCommentDomainType())
                .domainId(comment.getDomainId())
                .domainTitle(domainTitle)
                .isMine(isMine)
                .userInfo(userInfo)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
