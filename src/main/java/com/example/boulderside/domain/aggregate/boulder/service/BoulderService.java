package com.example.boulderside.domain.aggregate.boulder.service;

import com.example.boulderside.domain.aggregate.boulder.entity.Boulder;

public interface BoulderService {
	Boulder getBoulderById(Long boulderId);

	Boulder createBoulder(Boulder boulder);

	void deleteBoulder(Long boulderId);
}
