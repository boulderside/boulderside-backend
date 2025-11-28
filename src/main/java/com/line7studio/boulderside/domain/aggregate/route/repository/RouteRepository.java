package com.line7studio.boulderside.domain.aggregate.route.repository;

import com.line7studio.boulderside.domain.aggregate.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}