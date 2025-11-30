package com.line7studio.boulderside.domain.feature.boulder.service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.enums.BoulderSortType;
import com.line7studio.boulderside.domain.feature.boulder.repository.BoulderQueryRepository;
import com.line7studio.boulderside.domain.feature.boulder.repository.BoulderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoulderServiceImpl implements BoulderService {
	private final BoulderRepository boulderRepository;
	private final BoulderQueryRepository boulderQueryRepository;

    @Override
    public List<Boulder> getAllBoulders() {
        return boulderRepository.findAll();
    }

    @Override
	public Boulder getBoulderById(Long boulderId) {
		return boulderRepository.findById(boulderId)
			.orElseThrow(() -> new DomainException(ErrorCode.BOULDER_NOT_FOUND));
	}

	@Override
	public List<Boulder> getBouldersWithCursor(Long cursor, String subCursor, int size, BoulderSortType sortType) {
		return boulderQueryRepository.findBouldersWithCursor(sortType, cursor, subCursor, size);
	}

	@Override
	public Boulder createBoulder(Boulder boulder) {
		return  boulderRepository.save(boulder);
	}

	@Override
	public Boulder updateBoulder(Long boulderId, Long regionId, Long sectorId, String name, String description, Double latitude, Double longitude) {
		Boulder boulder = getBoulderById(boulderId);
		boulder.update(regionId, sectorId, name, description, latitude, longitude);
		return boulderRepository.save(boulder);
	}

	@Override
	public void deleteByBoulderId(Long boulderId) {
		Boulder boulder = getBoulderById(boulderId);
		boulderRepository.delete(boulder);
	}
}
