package com.line7studio.boulderside.controller.approach.response;

import com.line7studio.boulderside.common.dto.PointInfo;
import com.line7studio.boulderside.domain.approach.Approach;

import java.time.LocalDateTime;
import java.util.List;

public record ApproachResponse(
    Long id,
    Long boulderId,
    Integer orderIndex,
    String transportInfo,
    String parkingInfo,
    Integer duration,
    String tip,
    List<PointInfo> points,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ApproachResponse of(Approach approach, List<PointInfo> points) {
        return new ApproachResponse(
            approach.getId(),
            approach.getBoulderId(),
            approach.getOrderIndex(),
            approach.getTransportInfo(),
            approach.getParkingInfo(),
            approach.getDuration(),
            approach.getTip(),
            points,
            approach.getCreatedAt(),
            approach.getUpdatedAt()
        );
    }
}