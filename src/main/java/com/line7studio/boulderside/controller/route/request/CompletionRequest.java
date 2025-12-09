package com.line7studio.boulderside.controller.route.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompletionRequest {
	@NotNull
	private Boolean completed;

	@Size(max = 500)
	private String memo;
}
