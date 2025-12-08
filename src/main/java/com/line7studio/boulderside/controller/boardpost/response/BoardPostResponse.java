package com.line7studio.boulderside.controller.boardpost.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostResponse {
    private Long boardPostId;
    private Boolean isMine;
    private UserInfo userInfo;
    private String title;
    private String content;
    private Long viewCount;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    public static BoardPostResponse of(BoardPost boardPost, UserInfo userInfo, Boolean isMine, Long commentCount) {
        return BoardPostResponse.builder()
            .boardPostId(boardPost.getId())
            .isMine(isMine)
            .userInfo(userInfo)
            .title(boardPost.getTitle())
            .content(boardPost.getContent())
            .viewCount(boardPost.getViewCount())
            .commentCount(commentCount)
            .createdAt(boardPost.getCreatedAt())
            .updatedAt(boardPost.getUpdatedAt())
            .userId(userInfo.getId())
            .build();
    }
}
