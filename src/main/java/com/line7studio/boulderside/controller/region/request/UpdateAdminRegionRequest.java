package com.line7studio.boulderside.controller.region.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminRegionRequest {
    private String officialDistrictCode;

    @NotBlank(message = "시/도는 필수입니다.")
    private String province;

    @NotBlank(message = "시/군/구는 필수입니다.")
    private String city;

    @NotBlank(message = "지역 코드는 필수입니다.")
    private String regionCode;
}
