package com.line7studio.boulderside.controller.route.request;

import com.line7studio.boulderside.common.enums.Level;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRouteRequest(
    @NotNull(message = "연관 바위 ID는 필수입니다") Long boulderId,
    @NotBlank(message = "루트 이름은 필수입니다") String name,
    @NotBlank(message = "개척자 이름은 필수입니다") String pioneerName,
    @NotNull(message = "난이도는 필수입니다") Level routeLevel
) {}