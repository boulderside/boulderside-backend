package com.line7studio.boulderside.domain.aggregate.region.service;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.exception.DatabaseException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.aggregate.region.entity.Region;
import com.line7studio.boulderside.domain.aggregate.region.repository.RegionRepository;

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
}
