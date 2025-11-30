package com.line7studio.boulderside.domain.feature.route.interaction.completion.service;

import java.util.List;

import com.line7studio.boulderside.domain.feature.route.interaction.completion.entity.UserRouteCompletion;

public interface UserRouteCompletionService {
	UserRouteCompletion create(Long userId, Long routeId, boolean completed, String memo);

	UserRouteCompletion update(Long userId, Long routeId, boolean completed, String memo);

	UserRouteCompletion get(Long userId, Long routeId);

	List<UserRouteCompletion> getAll(Long userId);

	void delete(Long userId, Long routeId);
}
