package com.line7studio.boulderside.controller.region.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateAdminRegionRequest(
    String officialDistrictCode,
    @NotBlank(message = "시/도는 필수입니다.") String province,
    @NotBlank(message = "시/군/구는 필수입니다.") String city,
    @NotBlank(message = "지역 코드는 필수입니다.") String regionCode
) {}