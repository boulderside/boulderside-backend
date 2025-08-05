package com.line7studio.boulderside.domain.aggregate.boulder.service;

import java.util.List;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;

public interface BoulderQueryService {
	List<BoulderWithRegion> getBoulderWithRegionList(BoulderSortType sortType, Long cursor, Long cursorLikeCount, int size);

	BoulderWithRegion getBoulderWithRegion(Long boulderId);
}
