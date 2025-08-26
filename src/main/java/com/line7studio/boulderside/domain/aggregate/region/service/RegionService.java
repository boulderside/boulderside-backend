package com.line7studio.boulderside.domain.aggregate.region.service;

import com.line7studio.boulderside.domain.aggregate.region.entity.Region;

import java.util.List;

public interface RegionService {
	Region getRegionById(Long regionId);
	List<Region> getRegionsByIds(List<Long> regionIds);
	Region getRegionByProvinceAndCity(String province, String city);
}
