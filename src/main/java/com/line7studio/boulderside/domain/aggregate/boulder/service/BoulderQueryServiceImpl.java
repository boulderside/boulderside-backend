package com.line7studio.boulderside.domain.aggregate.boulder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.domain.aggregate.boulder.repository.BoulderQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoulderQueryServiceImpl implements BoulderQueryService {
	private final BoulderQueryRepository boulderQueryRepository;

	@Override
	public List<BoulderWithRegion> getBoulderWithRegionList(Long cursor, int size) {
		return boulderQueryRepository.findBouldersWithRegionAndCursor(cursor, size);
	}

	@Override
	public BoulderWithRegion getBoulderWithRegion(Long boulderId) {
		return boulderQueryRepository.findBouldersWithRegionByBoulderId(boulderId);
	}
}
