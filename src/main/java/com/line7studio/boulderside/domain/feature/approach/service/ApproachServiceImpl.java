package com.line7studio.boulderside.domain.feature.approach.service;

import com.line7studio.boulderside.common.exception.DatabaseException;
import com.line7studio.boulderside.common.exception.ErrorCode;
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
    public Approach getById(Long id) {
        return approachRepository.findById(id)
            .orElseThrow(() -> new DatabaseException(ErrorCode.APPROACH_NOT_FOUND));
    }

    @Override
    public void deleteById(Long id) {
        if (!approachRepository.existsById(id)) {
            throw new DatabaseException(ErrorCode.APPROACH_NOT_FOUND);
        }
        approachRepository.deleteById(id);
    }

    @Override
    public List<Approach> findByBoulderIdOrderByOrderIndexAsc(Long boulderId) {
        return approachRepository.findByBoulderIdOrderByOrderIndexAsc(boulderId);
    }

    @Override
    public List<Approach> findAll() {
        return approachRepository.findAll();
    }
}
