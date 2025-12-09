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
public class RegionServiceImpl implements RegionService {
	private final RegionRepository regionRepository;

	@Override
	public Region getRegionById(Long regionId) {
		return regionRepository.findById(regionId)
			.orElseThrow(() -> new DatabaseException(ErrorCode.REGION_NOT_FOUND));
	}

	@Override
	public List<Region> getRegionsByIds(List<Long> regionIds) {
		return regionRepository.findAllById(regionIds);
	}

	@Override
	public Region getRegionByProvinceAndCity(String province, String city) {
		return regionRepository.findByProvinceAndCity(province, city)
			.orElseThrow(() -> new DatabaseException(ErrorCode.REGION_NOT_FOUND));
	}

	@Override
	public List<Region> getAllRegions() {
		return regionRepository.findAll(Sort.by("province").ascending().and(Sort.by("city").ascending()));
	}

	@Override
	public Region saveRegion(Region region) {
		return regionRepository.save(region);
	}

	@Override
	public Region updateRegion(Long regionId, String officialDistrictCode, String province, String city, String regionCode) {
		Region region = getRegionById(regionId);
		region.update(officialDistrictCode, province, city, regionCode);
		return region;
	}

	@Override
	public void deleteRegion(Long regionId) {
		Region region = getRegionById(regionId);
		regionRepository.delete(region);
	}
}
