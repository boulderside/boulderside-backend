package com.line7studio.boulderside.controller.post.response;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostType;
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
	private Long id;
	private UserInfo userInfo;
	private String title;
	private String content;
	private PostType postType;
	private Integer viewCount;
	private LocalDate meetingDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static PostResponse of(Post post, UserInfo userInfo) {
		return PostResponse.builder()
			.id(post.getId())
            .userInfo(userInfo)
            .title(post.getTitle())
            .content(post.getContent())
			.postType(post.getPostType())
			.viewCount(post.getViewCount())
			.meetingDate(post.getMeetingDate())
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.build();
	}
}