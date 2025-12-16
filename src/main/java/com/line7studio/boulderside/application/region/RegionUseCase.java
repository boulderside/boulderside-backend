package com.line7studio.boulderside.application.region;

import com.line7studio.boulderside.controller.region.request.CreateAdminRegionRequest;
import com.line7studio.boulderside.controller.region.request.UpdateAdminRegionRequest;
import com.line7studio.boulderside.controller.region.response.RegionResponse;
import com.line7studio.boulderside.domain.feature.region.entity.Region;
import com.line7studio.boulderside.domain.feature.region.service.RegionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionUseCase {

    private final RegionService regionService;

    public List<RegionResponse> getRegions() {
        return regionService.getAllRegions().stream()
            .map(RegionResponse::from)
            .toList();
    }

    public RegionResponse getRegion(Long regionId) {
        return RegionResponse.from(regionService.getRegionById(regionId));
    }

    @Transactional
    public RegionResponse createRegion(CreateAdminRegionRequest request) {
        Region region = Region.builder()
            .officialDistrictCode(request.officialDistrictCode())
            .province(request.province())
            .city(request.city())
            .regionCode(request.regionCode())
            .build();
        Region saved = regionService.saveRegion(region);
        return RegionResponse.from(saved);
    }

    @Transactional
    public RegionResponse updateRegion(Long regionId, UpdateAdminRegionRequest request) {
        Region updated = regionService.updateRegion(
            regionId,
            request.officialDistrictCode(),
            request.province(),
            request.city(),
            request.regionCode());
        return RegionResponse.from(updated);
    }

    @Transactional
    public void deleteRegion(Long regionId) {
        regionService.deleteRegion(regionId);
    }
}
