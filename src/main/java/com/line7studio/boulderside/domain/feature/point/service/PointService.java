package com.line7studio.boulderside.domain.feature.point.service;

import com.line7studio.boulderside.domain.feature.point.Point;

import java.util.List;

public interface PointService {
    Point save(Point point);
    List<Point> saveAll(List<Point> points);
    List<Point> findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(List<Long> approachIdList);
}