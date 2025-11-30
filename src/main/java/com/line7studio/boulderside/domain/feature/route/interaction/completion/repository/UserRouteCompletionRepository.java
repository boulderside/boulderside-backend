package com.line7studio.boulderside.domain.feature.route.interaction.completion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.route.interaction.completion.entity.UserRouteCompletion;

public interface UserRouteCompletionRepository extends JpaRepository<UserRouteCompletion, Long> {
	Optional<UserRouteCompletion> findByUserIdAndRouteId(Long userId, Long routeId);

	List<UserRouteCompletion> findAllByUserId(Long userId);

	void deleteByUserIdAndRouteId(Long userId, Long routeId);
}
