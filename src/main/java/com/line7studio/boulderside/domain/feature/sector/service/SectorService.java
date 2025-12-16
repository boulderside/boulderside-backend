package com.line7studio.boulderside.domain.feature.sector.service;

import com.line7studio.boulderside.domain.feature.sector.entity.Sector;

import java.util.List;

public interface SectorService {
	Sector getSectorById(Long sectorId);
	List<Sector> getSectorsByIds(List<Long> sectorIdList);
	List<Sector> getAllSectors();
	Sector saveSector(Sector sector);
	Sector updateSector(Long sectorId, String sectorName, String areaCode);
	void deleteSector(Long sectorId);
}
