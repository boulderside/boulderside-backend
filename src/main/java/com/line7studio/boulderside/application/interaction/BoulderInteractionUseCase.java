package com.line7studio.boulderside.application.interaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.controller.boulder.response.BoulderLikeResponse;
import com.line7studio.boulderside.controller.boulder.response.LikedBoulderItemResponse;
import com.line7studio.boulderside.controller.boulder.response.LikedBoulderPageResponse;
import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.service.UserBoulderLikeService;
import com.line7studio.boulderside.domain.feature.boulder.service.BoulderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoulderInteractionUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final BoulderService boulderService;
	private final UserBoulderLikeService userBoulderLikeService;

	public BoulderLikeResponse toggleLike(Long userId, Long boulderId) {
		Boulder boulder = boulderService.getBoulderById(boulderId);

		UserBoulderLike userBoulderLike = UserBoulderLike.builder()
			.userId(userId)
			.boulderId(boulder.getId())
			.build();

		boolean liked = userBoulderLikeService.toggle(userBoulderLike);

		if (liked) {
			boulder.incrementLikeCount();
		} else {
			boulder.decrementLikeCount();
		}
		long likeCount = boulder.getLikeCount() != null ? boulder.getLikeCount() : 0L;

		return BoulderLikeResponse.of(boulderId, liked, likeCount);
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

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}
