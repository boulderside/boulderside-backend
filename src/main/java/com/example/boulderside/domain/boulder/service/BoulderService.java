package com.example.boulderside.domain.boulder.service;

import com.example.boulderside.domain.boulder.entity.Boulder;

public interface BoulderService {
	Boulder getBoulderById(Long boulderId);

	Boulder createBoulder(Boulder boulder);

	void deleteBoulder(Long boulderId);
}
