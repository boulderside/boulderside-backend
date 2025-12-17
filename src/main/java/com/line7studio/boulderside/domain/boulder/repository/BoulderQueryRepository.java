package com.line7studio.boulderside.domain.boulder.repository;

import java.util.List;

import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.enums.BoulderSortType;

public interface BoulderQueryRepository {
	List<Boulder> findBouldersWithCursor(BoulderSortType sortType, Long cursor, String subCursor, int size);
}