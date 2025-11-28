package com.line7studio.boulderside.domain.aggregate.boulder.service;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;

import java.util.List;

public interface BoulderService {
    List<Boulder> getAllBoulders();
	Boulder getBoulderById(Long boulderId);
	List<Boulder> getBouldersWithCursor(Long cursor, String subCursor, int size, BoulderSortType sortType);
	Boulder createBoulder(Boulder boulder);
	Boulder updateBoulder(Long boulderId, Long regionId, String name, String description, Double latitude, Double longitude);
	void deleteByBoulderId(Long boulderId);
}
