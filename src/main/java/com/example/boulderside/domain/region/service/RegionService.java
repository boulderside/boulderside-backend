package com.example.boulderside.domain.region.service;

import com.example.boulderside.domain.region.entity.Region;

public interface RegionService {
	Region getRegionBtId(Long regionId);

	Region getRegionByProvinceAndCity(String province, String city);
}
