package com.line7studio.boulderside.domain.instagram.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.instagram.Instagram;
import com.line7studio.boulderside.domain.instagram.repository.InstagramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstagramService {
	private final InstagramRepository instagramRepository;

	public Instagram save(Instagram instagram) {
		return instagramRepository.save(instagram);
	}

	public Instagram getById(Long id) {
		return instagramRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.INSTAGRAM_NOT_FOUND));
	}

	public Optional<Instagram> findByUrl(String url) {
		return instagramRepository.findByUrl(url);
	}

	public List<Instagram> findByUserId(Long userId) {
		return instagramRepository.findByUserId(userId);
	}

	public List<Instagram> findByUserIdWithCursor(Long userId, Long cursor, int size) {
		return instagramRepository.findByUserIdWithCursor(userId, cursor, PageRequest.of(0, size));
	}

	public List<Instagram> findAll() {
		return instagramRepository.findAll();
	}

	public List<Instagram> findAllWithCursor(Long cursor, int size) {
		return instagramRepository.findAllWithCursor(cursor, PageRequest.of(0, size));
	}

	public void deleteById(Long id) {
		if (!instagramRepository.existsById(id)) {
			throw new BusinessException(ErrorCode.INSTAGRAM_NOT_FOUND);
		}
		instagramRepository.deleteById(id);
	}

	public boolean existsById(Long id) {
		return instagramRepository.existsById(id);
	}
}