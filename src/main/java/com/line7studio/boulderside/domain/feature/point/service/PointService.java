package com.line7studio.boulderside.domain.feature.point.service;

import com.line7studio.boulderside.domain.feature.point.entity.Point;

import java.util.List;

public interface PointService {
    Point save(Point point);
    List<Point> saveAll(List<Point> points);
    List<Point> findByApproachIdOrderByOrderIndexAsc(Long approachId);
    List<Point> findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(List<Long> approachIdList);
    void deleteByApproachId(Long approachId);
}
