package com.line7studio.boulderside.controller.region.response;

import com.line7studio.boulderside.domain.region.Region;

public record RegionResponse(
    Long id,
    String officialDistrictCode,
    String province,
    String city,
    String regionCode
) {
    public static RegionResponse from(Region region) {
        return new RegionResponse(
            region.getId(),
            region.getOfficialDistrictCode(),
            region.getProvince(),
            region.getCity(),
            region.getRegionCode()
        );
    }
}