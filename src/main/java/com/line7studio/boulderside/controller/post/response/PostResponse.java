package com.line7studio.boulderside.controller.post.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.feature.post.entity.Post;
import com.line7studio.boulderside.domain.feature.post.enums.PostType;
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
public class PostResponse {
	private Long postId;
    private Boolean isMine;
    private UserInfo userInfo;
	private String title;
	private String content;
	private PostType postType;
	private Long viewCount;
	private Long commentCount;
	private LocalDate meetingDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static PostResponse of(Post post, UserInfo userInfo, Boolean isMine, Long commentCount) {
		return PostResponse.builder()
			.postId(post.getId())
            .isMine(isMine)
            .userInfo(userInfo)
            .title(post.getTitle())
            .content(post.getContent())
			.postType(post.getPostType())
			.viewCount(post.getViewCount())
			.commentCount(commentCount)
			.meetingDate(post.getMeetingDate())
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.build();
	}
}