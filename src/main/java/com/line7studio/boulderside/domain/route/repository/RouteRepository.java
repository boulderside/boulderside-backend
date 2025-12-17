package com.line7studio.boulderside.domain.route.repository;

import com.line7studio.boulderside.domain.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
	List<Route> findByBoulderIdOrderByIdAsc(Long boulderId);
}
