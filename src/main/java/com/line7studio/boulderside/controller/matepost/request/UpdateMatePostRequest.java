package com.line7studio.boulderside.controller.matepost.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMatePostRequest {

	@NotBlank(message = "제목은 필수입니다")
	private String title;

	@NotBlank(message = "내용은 필수입니다")
	private String content;

	@NotNull(message = "동행 날짜는 필수입니다")
	private LocalDate meetingDate;
}
