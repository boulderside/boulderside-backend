package com.line7studio.boulderside.domain.feature.route.repository;

import com.line7studio.boulderside.domain.feature.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
	List<Route> findByBoulderIdOrderByIdAsc(Long boulderId);
}
