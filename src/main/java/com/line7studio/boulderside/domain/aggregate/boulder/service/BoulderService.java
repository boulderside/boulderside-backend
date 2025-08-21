package com.line7studio.boulderside.domain.aggregate.boulder.service;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;

import java.util.List;

public interface BoulderService {
	Boulder getBoulderById(Long boulderId);

	Boulder createBoulder(Boulder boulder);

	void deleteByBoulderId(Long boulderId);

    List<Boulder> getAllBoulders();
}
