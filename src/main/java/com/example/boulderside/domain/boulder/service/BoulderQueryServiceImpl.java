package com.example.boulderside.domain.boulder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.boulderside.application.boulder.dto.BoulderWithRegion;
import com.example.boulderside.domain.boulder.repository.BoulderQueryRepository;

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
		return boulderQueryRepository.findBouldersWithRegionById(boulderId);
	}
}
