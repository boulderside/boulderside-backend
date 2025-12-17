package com.line7studio.boulderside.controller.sector.response;

import com.line7studio.boulderside.domain.feature.sector.Sector;

public record SectorResponse(
    Long id,
    String sectorName,
    String areaCode
) {
    public static SectorResponse from(Sector sector) {
        return new SectorResponse(
            sector.getId(),
            sector.getSectorName(),
            sector.getAreaCode()
        );
    }
}