package com.line7studio.boulderside.domain.feature.approach.repository;

import com.line7studio.boulderside.domain.feature.approach.entity.Approach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApproachRepository extends JpaRepository<Approach, Long> {
    List<Approach> findByBoulderIdOrderByOrderIndexAsc(Long boulderId);
}