package com.line7studio.boulderside.usecase.interaction;

import com.line7studio.boulderside.controller.boulder.response.BoulderLikeResponse;
import com.line7studio.boulderside.controller.boulder.response.LikedBoulderItemResponse;
import com.line7studio.boulderside.controller.boulder.response.LikedBoulderPageResponse;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.interaction.like.UserBoulderLike;
import com.line7studio.boulderside.domain.boulder.interaction.like.service.UserBoulderLikeService;
import com.line7studio.boulderside.domain.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.region.Region;
import com.line7studio.boulderside.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoulderInteractionUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final BoulderService boulderService;
	private final UserBoulderLikeService userBoulderLikeService;
	private final RegionService regionService;

	public BoulderLikeResponse toggleLike(Long userId, Long boulderId) {
		Boulder boulder = boulderService.getById(boulderId);

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
		Long nextCursor = hasNext && !likes.isEmpty() ? likes.getLast().getId() : null;

		if (likes.isEmpty()) {
			return LikedBoulderPageResponse.of(Collections.emptyList(), nextCursor, hasNext, 0);
		}

		// Boulder 정보 조회
		List<Long> boulderIds = likes.stream()
			.map(UserBoulderLike::getBoulderId)
			.distinct()
			.toList();

		Map<Long, Boulder> boulderMap = boulderService.getBouldersByIds(boulderIds).stream()
			.collect(Collectors.toMap(Boulder::getId, Function.identity()));

		// Region 정보 조회 (province, city)
		List<Long> regionIds = boulderMap.values().stream()
			.map(Boulder::getRegionId)
			.distinct()
			.toList();
		Map<Long, Region> regionMap = regionService.getRegionsByIds(regionIds).stream()
			.collect(Collectors.toMap(Region::getId, Function.identity()));

		// Response 생성
		List<LikedBoulderItemResponse> content = likes.stream()
			.map(like -> {
				Boulder boulder = boulderMap.get(like.getBoulderId());
				if (boulder == null) {
					return null;
				}
				Region region = regionMap.get(boulder.getRegionId());

				return LikedBoulderItemResponse.of(
					like,
					boulder,
					region != null ? region.getProvince() : null,
					region != null ? region.getCity() : null
				);
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
