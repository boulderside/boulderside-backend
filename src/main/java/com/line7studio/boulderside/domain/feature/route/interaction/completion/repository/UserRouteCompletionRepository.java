package com.line7studio.boulderside.domain.feature.route.interaction.completion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.route.interaction.completion.entity.UserRouteCompletion;

public interface UserRouteCompletionRepository extends JpaRepository<UserRouteCompletion, Long> {
}
