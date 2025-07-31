package com.example.boulderside.domain.boulder.service;

import java.util.List;

import com.example.boulderside.application.boulder.dto.BoulderWithRegion;

public interface BoulderQueryService {
	List<BoulderWithRegion> getBoulderWithRegionList(Long cursor, int size);

	BoulderWithRegion getBoulderWithRegion(Long boulderId);
}
