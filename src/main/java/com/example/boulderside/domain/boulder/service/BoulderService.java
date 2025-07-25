package com.example.boulderside.domain.boulder.service;

import java.util.List;

import com.example.boulderside.domain.boulder.entity.Boulder;

public interface BoulderService {
	Boulder getBoulderById(Long boulderId);

	List<Boulder> getBoulderList();
}
