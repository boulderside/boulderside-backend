package com.line7studio.boulderside.controller.matepost.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatePostResponse {
    private Long matePostId;
    private Boolean isMine;
    private UserInfo userInfo;
    private String title;
    private String content;
    private Long viewCount;
    private Long commentCount;
    private LocalDate meetingDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MatePostResponse of(MatePost matePost, UserInfo userInfo, Boolean isMine, Long commentCount) {
        return MatePostResponse.builder()
            .matePostId(matePost.getId())
            .isMine(isMine)
            .userInfo(userInfo)
            .title(matePost.getTitle())
            .content(matePost.getContent())
            .viewCount(matePost.getViewCount())
            .commentCount(commentCount)
            .meetingDate(matePost.getMeetingDate())
            .createdAt(matePost.getCreatedAt())
            .updatedAt(matePost.getUpdatedAt())
            .build();
    }
}
