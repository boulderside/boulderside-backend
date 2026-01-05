package com.line7studio.boulderside.controller.approach.request;

import jakarta.validation.constraints.Size;

public record CreatePointRequest(
    Integer orderIndex,

    @Size(max = 100, message = "포인트 이름은 100자 이하여야 합니다.")
    String name,

    @Size(max = 500, message = "포인트 설명은 500자 이하여야 합니다.")
    String description,

    @Size(max = 500, message = "포인트 노트는 500자 이하여야 합니다.")
    String note
) {}