package com.line7studio.boulderside.controller.route.request;

import com.line7studio.boulderside.common.enums.Level;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRouteRequest(
    @NotNull(message = "연관 바위 ID는 필수입니다.")
    Long boulderId,

    @NotBlank(message = "루트 이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "루트 이름은 1자 이상 100자 이하여야 합니다.")
    String name,

    @NotBlank(message = "개척자 이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "개척자 이름은 1자 이상 100자 이하여야 합니다.")
    String pioneerName,

    @NotNull(message = "난이도는 필수입니다.")
    Level routeLevel
) {}