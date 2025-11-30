package com.line7studio.boulderside.domain.feature.boulder.service;

import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.enums.BoulderSortType;

import java.util.List;

public interface BoulderService {
    List<Boulder> getAllBoulders();
	Boulder getBoulderById(Long boulderId);
	List<Boulder> getBouldersWithCursor(Long cursor, String subCursor, int size, BoulderSortType sortType);
	List<Boulder> getBouldersByIds(List<Long> boulderIds);
	Boulder createBoulder(Boulder boulder);
	Boulder updateBoulder(Long boulderId, Long regionId, Long sectorId, String name, String description, Double latitude, Double longitude);
	void deleteByBoulderId(Long boulderId);
}
