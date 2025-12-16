package com.line7studio.boulderside.domain.feature.region.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.exception.DatabaseException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.region.entity.Region;
import com.line7studio.boulderside.domain.feature.region.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
	private final RegionRepository regionRepository;

	public Region getRegionById(Long regionId) {
		return regionRepository.findById(regionId)
			.orElseThrow(() -> new DatabaseException(ErrorCode.REGION_NOT_FOUND));
	}

	public List<Region> getRegionsByIds(List<Long> regionIds) {
		return regionRepository.findAllById(regionIds);
	}

	public Region getRegionByProvinceAndCity(String province, String city) {
		return regionRepository.findByProvinceAndCity(province, city)
			.orElseThrow(() -> new DatabaseException(ErrorCode.REGION_NOT_FOUND));
	}

	public List<Region> getAllRegions() {
		return regionRepository.findAll(Sort.by("province").ascending().and(Sort.by("city").ascending()));
	}

	public Region saveRegion(Region region) {
		return regionRepository.save(region);
	}

	public Region updateRegion(Long regionId, String officialDistrictCode, String province, String city, String regionCode) {
		Region region = getRegionById(regionId);
		region.update(officialDistrictCode, province, city, regionCode);
		return region;
	}

	public void deleteRegion(Long regionId) {
		Region region = getRegionById(regionId);
		regionRepository.delete(region);
	}
}
