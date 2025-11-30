package com.line7studio.boulderside.application.interaction;

import com.line7studio.boulderside.controller.interaction.response.RouteCompletionResponse;
import com.line7studio.boulderside.domain.feature.route.interaction.completion.service.UserRouteCompletionService;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteInteractionUseCase {
	private final RouteService routeService;
	private final UserRouteCompletionService userRouteCompletionService;

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

	private void validateRoute(Long routeId) {
		routeService.getRouteById(routeId);
	}
}
