package com.line7studio.boulderside.domain.boulder.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.enums.BoulderSortType;
import com.line7studio.boulderside.domain.boulder.repository.BoulderQueryRepository;
import com.line7studio.boulderside.domain.boulder.repository.BoulderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoulderService {
	private final BoulderRepository boulderRepository;
	private final BoulderQueryRepository boulderQueryRepository;

    public List<Boulder> getAllBoulders() {
        return boulderRepository.findAll();
    }

	public Boulder getById(Long boulderId) {
		return boulderRepository.findById(boulderId)
			.orElseThrow(() -> new BusinessException(ErrorCode.BOULDER_NOT_FOUND));
	}

	/**
	 * 조회수를 증가시키고 업데이트된 Boulder를 반환합니다.
	 */
	public Boulder incrementViewCount(Long boulderId) {
		Boulder boulder = getById(boulderId);
		boulder.incrementViewCount();
		return boulder;
	}

	public List<Boulder> getBouldersWithCursor(Long cursor, String subCursor, int size, BoulderSortType sortType) {
		return boulderQueryRepository.findBouldersWithCursor(sortType, cursor, subCursor, size);
	}

	public List<Boulder> getBouldersByIds(List<Long> boulderIds) {
		return boulderRepository.findAllById(boulderIds);
	}

	/**
	 * Boulder를 저장합니다.
	 */
	public Boulder save(Boulder boulder) {
		return boulderRepository.save(boulder);
	}

	/**
	 * Boulder 정보를 업데이트합니다.
	 */
	public Boulder update(Long boulderId, Long regionId, Long sectorId, String name, String description, Double latitude, Double longitude) {
		Boulder boulder = getById(boulderId);
		boulder.update(regionId, sectorId, name, description, latitude, longitude);
		return boulder;
	}

	/**
	 * Boulder를 삭제합니다.
	 */
	public void delete(Long boulderId) {
		Boulder boulder = getById(boulderId);
		boulderRepository.delete(boulder);
	}
}
