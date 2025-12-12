package com.line7studio.boulderside.controller.project.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectRequest {
	private Long routeId;

	@NotNull
	private Boolean completed;

	@Size(max = 500)
	private String memo;

	@Valid
	private List<ProjectAttemptHistoryRequest> attemptHistories;
}
