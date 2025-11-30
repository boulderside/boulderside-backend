package com.line7studio.boulderside.domain.feature.route.interaction.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.route.interaction.like.entity.UserRouteLike;

import java.util.List;

public interface UserRouteLikeRepository extends JpaRepository<UserRouteLike, Long> {
	boolean existsByUserIdAndRouteId(Long userId, Long routeId);

	void deleteByUserIdAndRouteId(Long userId, Long routeId);

	long countByRouteId(Long routeId);

	void deleteAllByRouteId(Long routeId);

    List<UserRouteLike> findByUserIdAndRouteIdIn(Long userId, List<Long> routeIds);
}
