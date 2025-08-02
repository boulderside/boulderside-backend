package com.line7studio.boulderside.domain.aggregate.boulder.repository;

import java.util.List;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;

public interface BoulderQueryRepository {
	List<BoulderWithRegion> findBouldersWithRegionAndCursor(Long cursor, int size);

	BoulderWithRegion findBouldersWithRegionByBoulderId(Long boulderId);
}