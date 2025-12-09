package com.line7studio.boulderside.domain.feature.approach.service;

import com.line7studio.boulderside.domain.feature.approach.Approach;

import java.util.List;

public interface ApproachService {
    Approach save(Approach approach);
    Approach getById(Long id);
    void deleteById(Long id);
    List<Approach> findByBoulderIdOrderByOrderIndexAsc(Long boulderId);
    List<Approach> findAll();
}
