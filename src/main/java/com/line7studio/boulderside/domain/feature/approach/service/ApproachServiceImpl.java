package com.line7studio.boulderside.domain.feature.approach.service;

import com.line7studio.boulderside.domain.feature.approach.Approach;
import com.line7studio.boulderside.domain.feature.approach.repository.ApproachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApproachServiceImpl implements ApproachService {
    private final ApproachRepository approachRepository;

    @Override
    public Approach save(Approach approach) {
        return approachRepository.save(approach);
    }

    @Override
    public List<Approach> findByBoulderIdOrderByOrderIndexAsc(Long boulderId) {
        return approachRepository.findByBoulderIdOrderByOrderIndexAsc(boulderId);
    }
}