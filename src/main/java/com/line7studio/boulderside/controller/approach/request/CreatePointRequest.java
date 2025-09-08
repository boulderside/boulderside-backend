package com.line7studio.boulderside.controller.approach.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePointRequest {
    private Integer orderIndex;
    private String name;
    private String description;
    private String note;
}