package com.line7studio.boulderside.domain.association.like.service;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ValidationException;
import com.line7studio.boulderside.domain.association.like.entity.UserBoulderLike;
import com.line7studio.boulderside.domain.association.like.repository.UserBoulderLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBoulderLikeServiceImpl implements UserBoulderLikeService {
	private final UserBoulderLikeRepository userBoulderLikeRepository;

	@Override
	public boolean toggle(UserBoulderLike userBoulderLike) {
		Long userId = userBoulderLike.getUserId();
		Long boulderId = userBoulderLike.getBoulderId();
		if (userId == null || boulderId == null) {
			throw new ValidationException(ErrorCode.VALIDATION_FAILED);
		}

		boolean alreadyExists = userBoulderLikeRepository.existsByUserIdAndBoulderId(userId, boulderId);

		if (alreadyExists) {
			userBoulderLikeRepository.deleteByUserIdAndBoulderId(userId, boulderId);
			return false;
		} else {
			userBoulderLikeRepository.save(userBoulderLike);
			return true;
		}
	}

	@Override
	public long getCountByBoulderId(Long boulderId) {
		if (boulderId == null) {
			throw new ValidationException(ErrorCode.VALIDATION_FAILED);
		}
		return userBoulderLikeRepository.countByBoulderId(boulderId);
	}

	@Override
	public boolean getIsLikedByUserId(Long boulderId, Long userId) {
		return userBoulderLikeRepository.existsByUserIdAndBoulderId(userId, boulderId);
	}

	@Override
	public void deleteAllByBoulderId(Long boulderId) {
		if (boulderId == null) {
			throw new ValidationException(ErrorCode.VALIDATION_FAILED);
		}
		userBoulderLikeRepository.deleteAllByBoulderId(boulderId);
	}
}
