package com.line7studio.boulderside.application.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void toggleBoulderLike(Long userId, Long boulderId) {
		Boulder boulder = boulderService.getBoulderById(boulderId);

		UserBoulderLike userBoulderLike = UserBoulderLike.builder()
			.userId(userId)
			.boulderId(boulder.getId())
			.build();

		userBoulderLikeService.toggle(userBoulderLike);
	}
}
