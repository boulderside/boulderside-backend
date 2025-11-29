package com.line7studio.boulderside.controller.route.request;

import com.line7studio.boulderside.common.enums.Level;

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
public class UpdateRouteRequest {

	@NotNull(message = "연관 바위 ID는 필수입니다")
	private Long boulderId;

	@NotBlank(message = "루트 이름은 필수입니다")
	private String name;

	@NotBlank(message = "개척자 이름은 필수입니다")
	private String pioneerName;

	@NotNull(message = "난이도는 필수입니다")
	private Level routeLevel;
}
