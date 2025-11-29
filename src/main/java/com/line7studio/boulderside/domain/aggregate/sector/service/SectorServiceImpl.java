package com.line7studio.boulderside.domain.aggregate.sector.service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.aggregate.sector.Sector;
import com.line7studio.boulderside.domain.aggregate.sector.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
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
}
