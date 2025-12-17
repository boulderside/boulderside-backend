package com.line7studio.boulderside.domain.sector.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.sector.Sector;
import com.line7studio.boulderside.domain.sector.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorService {
	private final SectorRepository sectorRepository;

	public Sector getSectorById(Long sectorId) {
		return sectorRepository.findById(sectorId)
			.orElseThrow(() -> new BusinessException(ErrorCode.SECTOR_NOT_FOUND));
	}

	public List<Sector> getSectorsByIds(List<Long> sectorIdList) {
		if (sectorIdList == null || sectorIdList.isEmpty()) {
			return Collections.emptyList();
		}
		return sectorRepository.findAllById(sectorIdList);
	}

	public List<Sector> getAllSectors() {
		return sectorRepository.findAll(Sort.by("sectorName").ascending());
	}

	public Sector saveSector(Sector sector) {
		return sectorRepository.save(sector);
	}

	public Sector updateSector(Long sectorId, String sectorName, String areaCode) {
		Sector sector = getSectorById(sectorId);
		sector.update(sectorName, areaCode);
		return sector;
	}

	public void deleteSector(Long sectorId) {
		Sector sector = getSectorById(sectorId);
		sectorRepository.delete(sector);
	}
}
