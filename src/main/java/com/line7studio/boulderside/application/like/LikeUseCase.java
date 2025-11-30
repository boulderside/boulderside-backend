package com.line7studio.boulderside.application.like;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.controller.like.response.LikeResponse;
import com.line7studio.boulderside.controller.like.response.LikedBoulderItemResponse;
import com.line7studio.boulderside.controller.like.response.LikedBoulderPageResponse;
import com.line7studio.boulderside.controller.like.response.LikedRouteItemResponse;
import com.line7studio.boulderside.controller.like.response.LikedRoutePageResponse;
import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.service.UserBoulderLikeService;
import com.line7studio.boulderside.domain.feature.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.route.interaction.like.entity.UserRouteLike;
import com.line7studio.boulderside.domain.feature.route.interaction.like.service.UserRouteLikeService;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeUseCase {
	private static final int MAX_PAGE_SIZE = 50;

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

		return LikeResponse.of(boulderId, "BOULDER", isLiked, likeCount);
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

		return LikeResponse.of(routeId, "ROUTE", isLiked, likeCount);
	}

	@Transactional(readOnly = true)
	public LikedBoulderPageResponse getLikedBoulders(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<UserBoulderLike> likes = userBoulderLikeService.getLikesByUser(userId, cursor, pageSize + 1);

		boolean hasNext = likes.size() > pageSize;
		if (hasNext) {
			likes = likes.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !likes.isEmpty() ? likes.get(likes.size() - 1).getId() : null;

		List<Long> boulderIds = likes.stream()
			.map(UserBoulderLike::getBoulderId)
			.distinct()
			.toList();

		Map<Long, Boulder> boulderMap = boulderService.getBouldersByIds(boulderIds).stream()
			.collect(Collectors.toMap(Boulder::getId, Function.identity()));

		List<LikedBoulderItemResponse> content = likes.stream()
			.map(like -> {
				Boulder boulder = boulderMap.get(like.getBoulderId());
				return boulder != null ? LikedBoulderItemResponse.of(like, boulder) : null;
			})
			.filter(Objects::nonNull)
			.toList();

		return LikedBoulderPageResponse.of(content, nextCursor, hasNext, content.size());
	}

	@Transactional(readOnly = true)
	public LikedRoutePageResponse getLikedRoutes(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<UserRouteLike> likes = userRouteLikeService.getLikesByUser(userId, cursor, pageSize + 1);

		boolean hasNext = likes.size() > pageSize;
		if (hasNext) {
			likes = likes.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !likes.isEmpty() ? likes.get(likes.size() - 1).getId() : null;

		List<Long> routeIds = likes.stream()
			.map(UserRouteLike::getRouteId)
			.distinct()
			.toList();

		Map<Long, Route> routeMap = routeService.getRoutesByIds(routeIds).stream()
			.collect(Collectors.toMap(Route::getId, Function.identity()));

		List<LikedRouteItemResponse> content = likes.stream()
			.map(like -> {
				Route route = routeMap.get(like.getRouteId());
				return route != null ? LikedRouteItemResponse.of(like, route) : null;
			})
			.filter(Objects::nonNull)
			.toList();

		return LikedRoutePageResponse.of(content, nextCursor, hasNext, content.size());
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}
