package com.example.boulderside.domain.boulder.service;

import org.springframework.stereotype.Service;

import com.example.boulderside.common.exception.DomainException;
import com.example.boulderside.common.exception.ErrorCode;
import com.example.boulderside.domain.boulder.entity.Boulder;
import com.example.boulderside.domain.boulder.repository.BoulderRepository;

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
