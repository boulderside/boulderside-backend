package com.line7studio.boulderside.domain.feature.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	Optional<Project> findByUserIdAndRouteId(Long userId, Long routeId);

	Optional<Project> findByIdAndUserId(Long id, Long userId);

	List<Project> findAllByUserId(Long userId);

	void deleteByUserIdAndRouteId(Long userId, Long routeId);

	List<Project> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

	List<Project> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);
}
