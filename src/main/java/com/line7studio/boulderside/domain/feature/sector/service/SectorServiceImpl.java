package com.line7studio.boulderside.domain.feature.sector.service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.sector.entity.Sector;
import com.line7studio.boulderside.domain.feature.sector.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorServiceImpl implements SectorService {
	private final SectorRepository sectorRepository;

	@Override
	public Sector getSectorById(Long sectorId) {
		return sectorRepository.findById(sectorId)
			.orElseThrow(() -> new DomainException(ErrorCode.SECTOR_NOT_FOUND));
	}

	@Override
	public List<Sector> getSectorsByIds(List<Long> sectorIdList) {
		if (sectorIdList == null || sectorIdList.isEmpty()) {
			return Collections.emptyList();
		}
		return sectorRepository.findAllById(sectorIdList);
	}

	@Override
	public List<Sector> getAllSectors() {
		return sectorRepository.findAll(Sort.by("sectorName").ascending());
	}

	@Override
	public Sector saveSector(Sector sector) {
		return sectorRepository.save(sector);
	}

	@Override
	public Sector updateSector(Long sectorId, String sectorName, String areaCode) {
		Sector sector = getSectorById(sectorId);
		sector.update(sectorName, areaCode);
		return sector;
	}

	@Override
	public void deleteSector(Long sectorId) {
		Sector sector = getSectorById(sectorId);
		sectorRepository.delete(sector);
	}
}
