package com.line7studio.boulderside.domain.feature.approach.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.approach.entity.Approach;
import com.line7studio.boulderside.domain.feature.approach.repository.ApproachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApproachService {
    private final ApproachRepository approachRepository;

    public Approach save(Approach approach) {
        return approachRepository.save(approach);
    }

    public Approach getById(Long id) {
        return approachRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.APPROACH_NOT_FOUND));
    }

    public void deleteById(Long id) {
        if (!approachRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.APPROACH_NOT_FOUND);
        }
        approachRepository.deleteById(id);
    }

    public List<Approach> findByBoulderIdOrderByOrderIndexAsc(Long boulderId) {
        return approachRepository.findByBoulderIdOrderByOrderIndexAsc(boulderId);
    }

    public List<Approach> findAll() {
        return approachRepository.findAll();
    }
}
