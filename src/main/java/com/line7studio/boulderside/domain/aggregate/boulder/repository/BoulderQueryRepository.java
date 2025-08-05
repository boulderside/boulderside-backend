package com.line7studio.boulderside.domain.aggregate.boulder.repository;

import java.util.List;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;

public interface BoulderQueryRepository {
	List<BoulderWithRegion> findBouldersWithRegionAndCursor(BoulderSortType sortType, Long cursor, Long cursorLikeCount, int size);

	BoulderWithRegion findBouldersWithRegionByBoulderId(Long boulderId);
}