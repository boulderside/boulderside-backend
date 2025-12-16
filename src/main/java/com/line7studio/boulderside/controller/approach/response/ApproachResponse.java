package com.line7studio.boulderside.controller.approach.response;

import com.line7studio.boulderside.common.dto.PointInfo;
import com.line7studio.boulderside.domain.feature.approach.entity.Approach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproachResponse {
    private Long id;
    private Long boulderId;
    private Integer orderIndex;
    private String transportInfo;
    private String parkingInfo;
    private Integer duration;
    private String tip;
    private List<PointInfo> points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ApproachResponse of(Approach approach, List<PointInfo> points) {
        return ApproachResponse.builder()
                .id(approach.getId())
                .boulderId(approach.getBoulderId())
                .orderIndex(approach.getOrderIndex())
                .transportInfo(approach.getTransportInfo())
                .parkingInfo(approach.getParkingInfo())
                .duration(approach.getDuration())
                .tip(approach.getTip())
                .points(points)
                .createdAt(approach.getCreatedAt())
                .updatedAt(approach.getUpdatedAt())
                .build();
    }
}