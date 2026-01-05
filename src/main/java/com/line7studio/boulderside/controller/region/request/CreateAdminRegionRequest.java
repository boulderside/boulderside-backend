package com.line7studio.boulderside.controller.region.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAdminRegionRequest(
    @Size(max = 20, message = "행정구역 코드는 20자 이하여야 합니다.")
    String officialDistrictCode,

    @NotBlank(message = "시/도는 필수입니다.")
    @Size(min = 1, max = 50, message = "시/도는 1자 이상 50자 이하여야 합니다.")
    String province,

    String city,

    @NotBlank(message = "지역 코드는 필수입니다.")
    @Size(min = 1, max = 20, message = "지역 코드는 1자 이상 20자 이하여야 합니다.")
    String regionCode
) {}