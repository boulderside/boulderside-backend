package com.line7studio.boulderside.application.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.controller.like.response.LikeResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.association.like.entity.UserBoulderLike;
import com.line7studio.boulderside.domain.association.like.service.UserBoulderLikeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeUseCase {
	private final BoulderService boulderService;
	private final UserBoulderLikeService userBoulderLikeService;

	@Transactional
	public LikeResponse toggleBoulderLike(Long userId, Long boulderId) {
		Boulder boulder = boulderService.getBoulderById(boulderId);

		UserBoulderLike userBoulderLike = UserBoulderLike.builder()
			.userId(userId)
			.boulderId(boulder.getId())
			.build();

		boolean isLiked = userBoulderLikeService.toggle(userBoulderLike);
		long likeCount = userBoulderLikeService.getCountByBoulderId(boulderId);

		return LikeResponse.of(boulderId, isLiked, likeCount);
	}
}
