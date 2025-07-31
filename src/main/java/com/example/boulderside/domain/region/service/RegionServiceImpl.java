package com.example.boulderside.domain.region.service;

import org.springframework.stereotype.Service;

import com.example.boulderside.common.exception.DatabaseException;
import com.example.boulderside.common.exception.ErrorCode;
import com.example.boulderside.domain.region.entity.Region;
import com.example.boulderside.domain.region.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
	private final RegionRepository regionRepository;

	@Override
	public Region getRegionBtId(Long regionId) {
		return regionRepository.findById(regionId)
			.orElseThrow(() -> new DatabaseException(ErrorCode.REGION_NOT_FOUND));
	}

	@Override
	public Region getRegionByProvinceAndCity(String province, String city) {
		return regionRepository.findByProvinceAndCity(province, city)
			.orElseThrow(() -> new DatabaseException(ErrorCode.REGION_NOT_FOUND));
	}
}
