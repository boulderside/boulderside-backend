package com.line7studio.boulderside.controller.post.request;

import java.time.LocalDate;

import com.line7studio.boulderside.domain.aggregate.post.enums.PostType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {

	@NotBlank(message = "제목은 필수입니다")
	private String title;

	private String content;

	@NotNull(message = "게시글 유형은 필수입니다")
	private PostType postType;

	private LocalDate meetingDate;
}