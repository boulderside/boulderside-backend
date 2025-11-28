package com.line7studio.boulderside.domain.aggregate.boulder.repository;

import java.util.List;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;

public interface BoulderQueryRepository {
	List<Boulder> findBouldersWithCursor(BoulderSortType sortType, Long cursor, String subCursor, int size);
}