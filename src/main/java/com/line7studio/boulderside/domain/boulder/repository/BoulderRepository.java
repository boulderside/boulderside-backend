package com.line7studio.boulderside.domain.boulder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.boulder.Boulder;

public interface BoulderRepository extends JpaRepository<Boulder, Long> {
}