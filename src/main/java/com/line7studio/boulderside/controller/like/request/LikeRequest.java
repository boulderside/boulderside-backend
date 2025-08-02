package com.line7studio.boulderside.controller.like.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {

	@NotNull(message = "바위 ID는 필수입니다")
	private Long boulderId;
}