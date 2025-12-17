package com.line7studio.boulderside.domain.approach.repository;

import com.line7studio.boulderside.domain.approach.Approach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApproachRepository extends JpaRepository<Approach, Long> {
    List<Approach> findByBoulderIdOrderByOrderIndexAsc(Long boulderId);
}