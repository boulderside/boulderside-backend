package com.line7studio.boulderside.usecase.interaction;

import com.line7studio.boulderside.controller.instagram.response.InstagramLikeResponse;
import com.line7studio.boulderside.controller.instagram.response.LikedInstagramItemResponse;
import com.line7studio.boulderside.controller.instagram.response.LikedInstagramPageResponse;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.instagram.Instagram;
import com.line7studio.boulderside.domain.instagram.RouteInstagram;
import com.line7studio.boulderside.domain.instagram.interaction.like.UserInstagramLike;
import com.line7studio.boulderside.domain.instagram.interaction.like.service.UserInstagramLikeService;
import com.line7studio.boulderside.domain.instagram.service.InstagramService;
import com.line7studio.boulderside.domain.instagram.service.RouteInstagramService;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
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
public class InstagramInteractionUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final InstagramService instagramService;
	private final UserInstagramLikeService userInstagramLikeService;
	private final RouteInstagramService routeInstagramService;
	private final RouteService routeService;
	private final BoulderService boulderService;

	public InstagramLikeResponse toggleLike(Long userId, Long instagramId) {
		Instagram instagram = instagramService.getById(instagramId);

		UserInstagramLike userInstagramLike = UserInstagramLike.builder()
			.userId(userId)
			.instagramId(instagram.getId())
			.build();

		boolean liked = userInstagramLikeService.toggle(userInstagramLike);

		if (liked) {
			instagram.incrementLikeCount();
		} else {
			instagram.decrementLikeCount();
		}
		long likeCount = instagram.getLikeCount() != null ? instagram.getLikeCount() : 0L;

		return InstagramLikeResponse.of(instagramId, liked, likeCount);
	}

	@Transactional(readOnly = true)
	public LikedInstagramPageResponse getLikedInstagrams(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<UserInstagramLike> likes = userInstagramLikeService.getLikesByUser(userId, cursor, pageSize + 1);

		boolean hasNext = likes.size() > pageSize;
		if (hasNext) {
			likes = likes.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !likes.isEmpty() ? likes.getLast().getId() : null;

		if (likes.isEmpty()) {
			return LikedInstagramPageResponse.of(Collections.emptyList(), nextCursor, hasNext, 0);
		}

		// Instagram 정보 조회
		List<Long> instagramIds = likes.stream()
			.map(UserInstagramLike::getInstagramId)
			.distinct()
			.toList();

		Map<Long, Instagram> instagramMap = instagramIds.stream()
			.map(instagramService::getById)
			.collect(Collectors.toMap(Instagram::getId, Function.identity()));

		// RouteInfo 조회
		Map<Long, List<LikedInstagramItemResponse.RouteInfo>> routeInfosMap = instagramIds.stream()
			.collect(Collectors.toMap(
				instagramId -> instagramId,
				this::getRouteInfosByInstagramId
			));

		// Response 생성
		List<LikedInstagramItemResponse> content = likes.stream()
			.map(like -> {
				Instagram instagram = instagramMap.get(like.getInstagramId());
				if (instagram == null) {
					return null;
				}

				List<LikedInstagramItemResponse.RouteInfo> routes = routeInfosMap.getOrDefault(
					like.getInstagramId(),
					Collections.emptyList()
				);
				return LikedInstagramItemResponse.of(like, instagram, routes);
			})
			.filter(Objects::nonNull)
			.toList();

		return LikedInstagramPageResponse.of(content, nextCursor, hasNext, content.size());
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}

	private List<Long> getRouteIdsByInstagramId(Long instagramId) {
		return routeInstagramService.findByInstagramId(instagramId).stream()
			.map(RouteInstagram::getRouteId)
			.collect(Collectors.toList());
	}

	private List<LikedInstagramItemResponse.RouteInfo> getRouteInfosByInstagramId(Long instagramId) {
		return routeInstagramService.findByInstagramId(instagramId).stream()
			.map(ri -> {
				Route route = routeService.getById(ri.getRouteId());
				Boulder boulder = boulderService.getById(route.getBoulderId());
				return LikedInstagramItemResponse.RouteInfo.of(
					route.getId(),
					route.getName(),
					boulder.getName()
				);
			})
			.toList();
	}
}