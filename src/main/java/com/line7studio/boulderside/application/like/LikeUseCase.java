package com.line7studio.boulderside.application.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.controller.like.response.LikeResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import com.line7studio.boulderside.domain.aggregate.route.service.RouteService;
import com.line7studio.boulderside.domain.association.like.entity.UserBoulderLike;
import com.line7studio.boulderside.domain.association.like.entity.UserRouteLike;
import com.line7studio.boulderside.domain.association.like.service.UserBoulderLikeService;
import com.line7studio.boulderside.domain.association.like.service.UserRouteLikeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeUseCase {
	private final BoulderService boulderService;
	private final RouteService routeService;
	private final UserBoulderLikeService userBoulderLikeService;
	private final UserRouteLikeService userRouteLikeService;

	@Transactional
	public LikeResponse toggleBoulderLike(Long userId, Long boulderId) {
		Boulder boulder = boulderService.getBoulderById(boulderId);

		UserBoulderLike userBoulderLike = UserBoulderLike.builder()
			.userId(userId)
			.boulderId(boulder.getId())
			.build();

		boolean isLiked = userBoulderLikeService.toggle(userBoulderLike);

		if (isLiked) {
			boulder.incrementLikeCount();
		} else {
			boulder.decrementLikeCount();
		}
		long likeCount = boulder.getLikeCount() != null ? boulder.getLikeCount() : 0L;

		return LikeResponse.of(boulderId, isLiked, likeCount);
	}

	@Transactional
	public LikeResponse toggleRouteLike(Long userId, Long routeId) {
		Route route = routeService.getRouteById(routeId);

		UserRouteLike userRouteLike = UserRouteLike.builder()
			.userId(userId)
			.routeId(route.getId())
			.build();

		boolean isLiked = userRouteLikeService.toggle(userRouteLike);

		if (isLiked) {
			route.incrementLikeCount();
		} else {
			route.decrementLikeCount();
		}
		long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;

		return LikeResponse.of(routeId, isLiked, likeCount);
	}
}
