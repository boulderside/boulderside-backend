package com.line7studio.boulderside.controller.sector.response;

import com.line7studio.boulderside.domain.feature.sector.entity.Sector;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SectorResponse {
    private Long id;
    private String sectorName;
    private String areaCode;

    public static SectorResponse from(Sector sector) {
        return SectorResponse.builder()
            .id(sector.getId())
            .sectorName(sector.getSectorName())
            .areaCode(sector.getAreaCode())
            .build();
    }
}
