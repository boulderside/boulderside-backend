package com.line7studio.boulderside.domain.aggregate.point.service;

import com.line7studio.boulderside.domain.aggregate.point.Point;

import java.util.List;

public interface PointService {
    Point save(Point point);
    List<Point> saveAll(List<Point> points);
    List<Point> findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(List<Long> approachIdList);
}