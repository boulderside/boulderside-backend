package com.example.boulderside.domain.aggregate.region.service;

import com.example.boulderside.domain.aggregate.region.entity.Region;

public interface RegionService {
	Region getRegionByProvinceAndCity(String province, String city);
}
