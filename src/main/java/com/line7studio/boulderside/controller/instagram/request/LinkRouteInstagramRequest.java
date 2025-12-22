package com.line7studio.boulderside.controller.instagram.request;

import jakarta.validation.constraints.NotNull;

public record LinkRouteInstagramRequest(
	@NotNull(message = "루트 ID는 필수입니다.")
	Long routeId,

	@NotNull(message = "Instagram ID는 필수입니다.")
	Long instagramId
) {
}