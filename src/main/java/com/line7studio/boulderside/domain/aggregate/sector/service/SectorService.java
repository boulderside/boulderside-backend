package com.line7studio.boulderside.domain.aggregate.sector.service;

import com.line7studio.boulderside.domain.aggregate.sector.Sector;

import java.util.List;

public interface SectorService {
	Sector getSectorById(Long sectorId);
	List<Sector> getSectorsByIds(List<Long> sectorIdList);
}
