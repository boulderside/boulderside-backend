package com.line7studio.boulderside.application.interaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.controller.route.response.LikedRouteItemResponse;
import com.line7studio.boulderside.controller.route.response.LikedRoutePageResponse;
import com.line7studio.boulderside.controller.route.response.RouteCompletionResponse;
import com.line7studio.boulderside.controller.route.response.RouteLikeResponse;
import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.route.interaction.completion.service.UserRouteCompletionService;
import com.line7studio.boulderside.domain.feature.route.interaction.like.entity.UserRouteLike;
import com.line7studio.boulderside.domain.feature.route.interaction.like.service.UserRouteLikeService;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteInteractionUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final RouteService routeService;
	private final UserRouteCompletionService userRouteCompletionService;
	private final UserRouteLikeService userRouteLikeService;

	@Transactional(readOnly = true)
	public RouteCompletionResponse getCompletion(Long userId, Long routeId) {
		return RouteCompletionResponse.from(userRouteCompletionService.get(userId, routeId));
	}

	public RouteCompletionResponse createCompletion(Long userId, Long routeId, boolean completed, String memo) {
		validateRoute(routeId);
		return RouteCompletionResponse.from(
			userRouteCompletionService.create(userId, routeId, completed, memo));
	}

	public RouteCompletionResponse updateCompletion(Long userId, Long routeId, boolean completed, String memo) {
		validateRoute(routeId);
		return RouteCompletionResponse.from(
			userRouteCompletionService.update(userId, routeId, completed, memo));
	}

	public void deleteCompletion(Long userId, Long routeId) {
		validateRoute(routeId);
		userRouteCompletionService.delete(userId, routeId);
	}

	@Transactional(readOnly = true)
	public List<RouteCompletionResponse> getAllCompletions(Long userId) {
		return userRouteCompletionService.getAll(userId)
			.stream()
			.map(RouteCompletionResponse::from)
			.toList();
	}

	public RouteLikeResponse toggleLike(Long userId, Long routeId) {
		Route route = routeService.getRouteById(routeId);

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

	private void validateRoute(Long routeId) {
		routeService.getRouteById(routeId);
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}
