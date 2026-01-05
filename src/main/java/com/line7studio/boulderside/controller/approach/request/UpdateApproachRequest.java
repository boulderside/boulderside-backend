package com.line7studio.boulderside.controller.approach.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateApproachRequest(
    @NotNull(message = "바위 ID는 필수입니다.")
    Long boulderId,

    Integer orderIndex,

    @Size(max = 500, message = "교통정보는 500자 이하여야 합니다.")
    String transportInfo,

    @Size(max = 500, message = "주차정보는 500자 이하여야 합니다.")
    String parkingInfo,

    Integer duration,

    @Size(max = 500, message = "팁은 500자 이하여야 합니다.")
    String tip,

    List<CreatePointRequest> points
) {}