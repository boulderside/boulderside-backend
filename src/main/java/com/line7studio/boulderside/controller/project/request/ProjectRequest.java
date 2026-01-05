package com.line7studio.boulderside.controller.project.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProjectRequest(
    Long routeId,

    @NotNull(message = "완료 여부는 필수입니다.")
    Boolean completed,

    @Size(max = 500, message = "메모는 500자 이하여야 합니다.")
    String memo,

    @Valid
    List<SessionRequest> sessions
) {}
