package com.example.boulderside.domain.association.like.service;

import org.springframework.stereotype.Service;

import com.example.boulderside.common.exception.ErrorCode;
import com.example.boulderside.common.exception.ValidationException;
import com.example.boulderside.domain.association.like.entity.UserBoulderLike;
import com.example.boulderside.domain.association.like.repository.UserBoulderLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBoulderLikeServiceImpl implements UserBoulderLikeService {
	private final UserBoulderLikeRepository userBoulderLikeRepository;

	@Override
	public void toggle(UserBoulderLike userBoulderLike) {
		Long userId = userBoulderLike.getUserId();
		Long boulderId = userBoulderLike.getBoulderId();
		if (userId == null || boulderId == null) {
			throw new ValidationException(ErrorCode.MISSING_REQUIRED_FIELD);
		}

		boolean alreadyExists = userBoulderLikeRepository.existsByUserIdAndBoulderId(userId, boulderId);

		if (alreadyExists) {
			userBoulderLikeRepository.deleteByUserIdAndBoulderId(userId, boulderId);
		} else {
			userBoulderLikeRepository.save(userBoulderLike);
		}
	}
}
