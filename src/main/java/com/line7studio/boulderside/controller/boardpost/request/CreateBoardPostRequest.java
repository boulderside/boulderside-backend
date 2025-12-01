package com.line7studio.boulderside.controller.boardpost.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardPostRequest {
	@NotBlank(message = "제목은 필수입니다")
	private String title;

	@NotBlank(message = "내용은 필수입니다")
	private String content;
}
