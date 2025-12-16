package com.line7studio.boulderside.domain.feature.point.service;

import com.line7studio.boulderside.domain.feature.point.entity.Point;
import com.line7studio.boulderside.domain.feature.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;

    @Override
    public Point save(Point point) {
        return pointRepository.save(point);
    }

    @Override
    public List<Point> saveAll(List<Point> points) {
        return pointRepository.saveAll(points);
    }

    @Override
    public List<Point> findByApproachIdOrderByOrderIndexAsc(Long approachId) {
        return pointRepository.findByApproachIdOrderByOrderIndexAsc(approachId);
    }

    @Override
    public List<Point> findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(List<Long> approachIdList) {
        return pointRepository.findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(approachIdList);
    }

    @Override
    public void deleteByApproachId(Long approachId) {
        pointRepository.deleteByApproachId(approachId);
    }
}
