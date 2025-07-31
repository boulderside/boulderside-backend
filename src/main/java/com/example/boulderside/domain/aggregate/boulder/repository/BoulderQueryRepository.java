package com.example.boulderside.domain.aggregate.boulder.repository;

import java.util.List;

import com.example.boulderside.application.boulder.dto.BoulderWithRegion;

public interface BoulderQueryRepository {
	List<BoulderWithRegion> findBouldersWithRegionAndCursor(Long cursor, int size);

	BoulderWithRegion findBouldersWithRegionById(Long boulderId);
}