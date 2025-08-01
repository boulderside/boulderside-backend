package com.line7studio.boulderside.domain.association.completion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.association.completion.entity.UserRouteCompletion;

public interface UserRouteCompletionRepository extends JpaRepository<UserRouteCompletion, Long> {
}
