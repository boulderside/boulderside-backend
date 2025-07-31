package com.example.boulderside.domain.boulder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.domain.boulder.entity.Boulder;

public interface BoulderRepository extends JpaRepository<Boulder, Long> {
}