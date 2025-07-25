package com.example.boulderside.domain.boulder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.domain.boulder.entity.Boulder;

public interface BoulderRepository extends JpaRepository<Boulder, Long> {
	Optional<Boulder> getBoulderById(Long id);
}
