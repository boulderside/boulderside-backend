package com.line7studio.boulderside.common.dto;

import com.line7studio.boulderside.domain.point.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointInfo {
    private Long id;
    private Integer orderIndex;
    private String name;
    private String description;
    private String note;
    private List<ImageInfo> images;

    public static PointInfo of(Point point, List<ImageInfo> images) {
        return PointInfo.builder()
                .id(point.getId())
                .orderIndex(point.getOrderIndex())
                .name(point.getName())
                .description(point.getDescription())
                .note(point.getNote())
                .images(images)
                .build();
    }
}