package com.line7studio.boulderside.controller.approach.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApproachRequest {
	@NotNull(message = "바위 ID는 필수입니다")
	private Long boulderId;

	private Integer orderIndex;
	private String transportInfo;
	private String parkingInfo;
	private Integer duration;
	private String tip;

	private List<CreatePointRequest> points;
}
