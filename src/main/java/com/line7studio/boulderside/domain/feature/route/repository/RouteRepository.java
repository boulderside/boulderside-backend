package com.line7studio.boulderside.domain.feature.route.repository;

import com.line7studio.boulderside.domain.feature.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}