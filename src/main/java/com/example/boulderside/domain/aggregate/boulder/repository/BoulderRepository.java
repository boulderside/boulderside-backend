package com.example.boulderside.domain.aggregate.boulder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.domain.aggregate.boulder.entity.Boulder;

public interface BoulderRepository extends JpaRepository<Boulder, Long> {
}