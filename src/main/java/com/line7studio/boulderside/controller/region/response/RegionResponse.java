package com.line7studio.boulderside.controller.region.response;

import com.line7studio.boulderside.domain.feature.region.entity.Region;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionResponse {
    private Long id;
    private String officialDistrictCode;
    private String province;
    private String city;
    private String regionCode;

    public static RegionResponse from(Region region) {
        return RegionResponse.builder()
            .id(region.getId())
            .officialDistrictCode(region.getOfficialDistrictCode())
            .province(region.getProvince())
            .city(region.getCity())
            .regionCode(region.getRegionCode())
            .build();
    }
}
