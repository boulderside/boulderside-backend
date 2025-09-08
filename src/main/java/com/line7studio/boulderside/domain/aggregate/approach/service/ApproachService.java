package com.line7studio.boulderside.domain.aggregate.approach.service;

import com.line7studio.boulderside.domain.aggregate.approach.Approach;

import java.util.List;

public interface ApproachService {
    Approach save(Approach approach);
    List<Approach> findByBoulderIdOrderByOrderIndexAsc(Long boulderId);
}