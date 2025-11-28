package com.line7studio.boulderside.domain.aggregate.point.repository;

import com.line7studio.boulderside.domain.aggregate.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByApproachIdOrderByOrderIndexAsc(Long approachId);
    List<Point> findAllByApproachIdInOrderByApproachIdAscOrderIndexAsc(List<Long> approachIdList);
}