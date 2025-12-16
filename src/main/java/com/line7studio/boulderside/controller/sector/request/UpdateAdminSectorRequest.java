package com.line7studio.boulderside.controller.sector.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateAdminSectorRequest(
    @NotBlank(message = "섹터 이름은 필수입니다.") String sectorName,
    @NotBlank(message = "구역 코드는 필수입니다.") String areaCode
) {}