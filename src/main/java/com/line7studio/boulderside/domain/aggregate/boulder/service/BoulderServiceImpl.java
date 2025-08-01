package com.line7studio.boulderside.domain.aggregate.boulder.service;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.repository.BoulderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoulderServiceImpl implements BoulderService {
	private final BoulderRepository boulderRepository;

	@Override
	public Boulder getBoulderById(Long boulderId) {
		return boulderRepository.findById(boulderId)
			.orElseThrow(() -> new DomainException(ErrorCode.BOULDER_NOT_FOUND));
	}

	@Override
	public Boulder createBoulder(Boulder boulder) {
		return boulderRepository.save(boulder);
	}

	@Override
	public void deleteBoulder(Long boulderId) {
		Boulder boulder = getBoulderById(boulderId);
		boulderRepository.delete(boulder);
	}
}
