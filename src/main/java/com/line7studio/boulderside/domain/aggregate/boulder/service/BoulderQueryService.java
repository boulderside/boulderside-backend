package com.line7studio.boulderside.domain.aggregate.boulder.service;

import java.util.List;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;

public interface BoulderQueryService {
	List<BoulderWithRegion> getBoulderWithRegionList(Long cursor, int size);

	BoulderWithRegion getBoulderWithRegion(Long boulderId);
}
