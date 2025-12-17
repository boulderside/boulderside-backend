package com.line7studio.boulderside.controller.approach.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateApproachRequest(
    @NotNull(message = "바위 ID는 필수입니다") Long boulderId,
    Integer orderIndex,
    String transportInfo,
    String parkingInfo,
    Integer duration,
    String tip,
    List<CreatePointRequest> points
) {}