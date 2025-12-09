package com.line7studio.boulderside.domain.feature.region.service;

import com.line7studio.boulderside.domain.feature.region.entity.Region;

import java.util.List;

public interface RegionService {
	Region getRegionById(Long regionId);
	List<Region> getRegionsByIds(List<Long> regionIds);
	Region getRegionByProvinceAndCity(String province, String city);
	List<Region> getAllRegions();
	Region saveRegion(Region region);
	Region updateRegion(Long regionId, String officialDistrictCode, String province, String city, String regionCode);
	void deleteRegion(Long regionId);
}
