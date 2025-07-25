package com.example.boulderside.domain.boulder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.boulderside.common.exception.DatabaseException;
import com.example.boulderside.common.exception.ErrorCode;
import com.example.boulderside.domain.boulder.entity.Boulder;
import com.example.boulderside.domain.boulder.repository.BoulderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoulderServiceImpl implements BoulderService {

	private final BoulderRepository boulderRepository;

	@Override
	public Boulder getBoulderById(Long id) {
		return boulderRepository.findById(id)
			.orElseThrow(() -> new DatabaseException(ErrorCode.BOULDER_NOT_FOUND));
	}

	@Override
	public List<Boulder> getBoulderList() {
		return boulderRepository.findAll();
	}
}
