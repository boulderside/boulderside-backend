package com.line7studio.boulderside.domain.aggregate.boulder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;

public interface BoulderRepository extends JpaRepository<Boulder, Long> {
}