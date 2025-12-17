package com.line7studio.boulderside.domain.feature.point.service;

import com.line7studio.boulderside.domain.feature.point.Point;
import com.line7studio.boulderside.domain.feature.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    public Point save(Point point) {
        return pointRepository.save(point);
    }

    public List<Point> saveAll(List<Point> points) {
        return pointRepository.saveAll(points);
    }

    public List<Point> findByApproachIdOrderByOrderIndexAsc(Long approachId) {
        return pointRepository.findByApproachIdOrderByOrderIndexAsc(approachId);
    }

    public List<Point> findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(List<Long> approachIdList) {
        return pointRepository.findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(approachIdList);
    }

    public void deleteByApproachId(Long approachId) {
        pointRepository.deleteByApproachId(approachId);
    }
}
