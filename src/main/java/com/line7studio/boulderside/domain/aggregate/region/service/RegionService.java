package com.line7studio.boulderside.domain.aggregate.region.service;

import com.line7studio.boulderside.domain.aggregate.region.entity.Region;

public interface RegionService {
	Region getRegionByProvinceAndCity(String province, String city);
}
