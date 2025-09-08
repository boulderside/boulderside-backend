package com.line7studio.boulderside.controller.approach.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApproachRequest {
    @NotNull(message = "바위 ID는 필수입니다")
    private Long boulderId;

    private Integer orderIndex;
    private String transportInfo;
    private String parkingInfo;
    private Integer duration;
    private String tip;

    private List<CreatePointRequest> points;
}