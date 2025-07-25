package com.example.boulderside.feature.like.service;

import org.springframework.stereotype.Service;

import com.example.boulderside.common.exception.BusinessException;
import com.example.boulderside.common.exception.ErrorCode;
import com.example.boulderside.common.exception.ValidationException;
import com.example.boulderside.feature.like.entity.UserBoulderLike;
import com.example.boulderside.feature.like.repository.UserBoulderLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBoulderLikeServiceImpl implements UserBoulderLikeService {
	private final UserBoulderLikeRepository userBoulderLikeRepository;

	@Override
	public void save(UserBoulderLike userBoulderLike) {
		if (userBoulderLike == null || userBoulderLike.getUserId() == null || userBoulderLike.getBoulderId() == null) {
			throw new ValidationException(ErrorCode.MISSING_REQUIRED_FIELD);
		}

		boolean alreadyExists = userBoulderLikeRepository.existsByUserIdAndBoulderId(
			userBoulderLike.getUserId(),
			userBoulderLike.getBoulderId()
		);

		if (alreadyExists) {
			throw new BusinessException(ErrorCode.ALREADY_LIKED);
		}
		userBoulderLikeRepository.save(userBoulderLike);
	}
}
