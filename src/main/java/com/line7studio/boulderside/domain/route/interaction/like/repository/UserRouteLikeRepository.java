package com.line7studio.boulderside.domain.route.interaction.like.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.route.interaction.like.UserRouteLike;

public interface UserRouteLikeRepository extends JpaRepository<UserRouteLike, Long> {
	boolean existsByUserIdAndRouteId(Long userId, Long routeId);

	void deleteByUserIdAndRouteId(Long userId, Long routeId);

	long countByRouteId(Long routeId);

	void deleteAllByRouteId(Long routeId);

	List<UserRouteLike> findByUserIdAndRouteIdIn(Long userId, List<Long> routeIds);

	List<UserRouteLike> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

	List<UserRouteLike> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);
}
