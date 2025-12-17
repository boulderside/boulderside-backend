package com.line7studio.boulderside.usecase.interaction;

import com.line7studio.boulderside.controller.route.response.LikedRouteItemResponse;
import com.line7studio.boulderside.controller.route.response.LikedRoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteLikeResponse;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.interaction.like.entity.UserRouteLike;
import com.line7studio.boulderside.domain.route.interaction.like.service.UserRouteLikeService;
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
public class RouteInteractionUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final RouteService routeService;
	private final UserRouteLikeService userRouteLikeService;
	private final BoulderService boulderService;

	public RouteLikeResponse toggleLike(Long userId, Long routeId) {
		Route route = routeService.getById(routeId);

		UserRouteLike userRouteLike = UserRouteLike.builder()
			.userId(userId)
			.routeId(route.getId())
			.build();

		boolean liked = userRouteLikeService.toggle(userRouteLike);

		if (liked) {
			route.incrementLikeCount();
		} else {
			route.decrementLikeCount();
		}
		long likeCount = route.getLikeCount() != null ? route.getLikeCount() : 0L;

		return RouteLikeResponse.of(routeId, liked, likeCount);
	}

	@Transactional(readOnly = true)
	public LikedRoutePageResponse getLikedRoutes(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<UserRouteLike> likes = userRouteLikeService.getLikesByUser(userId, cursor, pageSize + 1);

		boolean hasNext = likes.size() > pageSize;
		if (hasNext) {
			likes = likes.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !likes.isEmpty() ? likes.getLast().getId() : null;

		if (likes.isEmpty()) {
			return LikedRoutePageResponse.of(Collections.emptyList(), nextCursor, hasNext, 0);
		}

		// Route 정보 조회
		List<Long> routeIds = likes.stream()
			.map(UserRouteLike::getRouteId)
			.distinct()
			.toList();

		Map<Long, Route> routeMap = routeService.getRoutesByIds(routeIds).stream()
			.collect(Collectors.toMap(Route::getId, Function.identity()));

		// Boulder 정보 조회 (바위 이름)
		List<Long> boulderIds = routeMap.values().stream()
			.map(Route::getBoulderId)
			.distinct()
			.toList();
		Map<Long, Boulder> boulderMap = boulderService.getBouldersByIds(boulderIds).stream()
			.collect(Collectors.toMap(Boulder::getId, Function.identity()));

		// Response 생성
		List<LikedRouteItemResponse> content = likes.stream()
			.map(like -> {
				Route route = routeMap.get(like.getRouteId());
				if (route == null) {
					return null;
				}
				Boulder boulder = boulderMap.get(route.getBoulderId());
				String boulderName = boulder != null ? boulder.getName() : null;

				return LikedRouteItemResponse.of(like, route, boulderName);
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
