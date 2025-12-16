package com.line7studio.boulderside.application.sector;

import com.line7studio.boulderside.controller.sector.request.CreateAdminSectorRequest;
import com.line7studio.boulderside.controller.sector.request.UpdateAdminSectorRequest;
import com.line7studio.boulderside.controller.sector.response.SectorResponse;
import com.line7studio.boulderside.domain.feature.sector.entity.Sector;
import com.line7studio.boulderside.domain.feature.sector.service.SectorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectorUseCase {

    private final SectorService sectorService;

    public List<SectorResponse> getSectors() {
        return sectorService.getAllSectors().stream()
            .map(SectorResponse::from)
            .toList();
    }

    public SectorResponse getSector(Long sectorId) {
        return SectorResponse.from(sectorService.getSectorById(sectorId));
    }

    @Transactional
    public SectorResponse createSector(CreateAdminSectorRequest request) {
        Sector sector = Sector.builder()
            .sectorName(request.getSectorName())
            .areaCode(request.getAreaCode())
            .build();
        return SectorResponse.from(sectorService.saveSector(sector));
    }

    @Transactional
    public SectorResponse updateSector(Long sectorId, UpdateAdminSectorRequest request) {
        Sector updated = sectorService.updateSector(sectorId, request.getSectorName(), request.getAreaCode());
        return SectorResponse.from(updated);
    }

    @Transactional
    public void deleteSector(Long sectorId) {
        sectorService.deleteSector(sectorId);
    }
}
