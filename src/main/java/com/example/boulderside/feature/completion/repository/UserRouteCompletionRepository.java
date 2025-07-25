package com.example.boulderside.feature.completion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.feature.completion.entity.UserRouteCompletion;

public interface UserRouteCompletionRepository extends JpaRepository<UserRouteCompletion, Long> {
}
