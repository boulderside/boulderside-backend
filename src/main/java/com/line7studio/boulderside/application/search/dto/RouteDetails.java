package com.line7studio.boulderside.application.search.dto;

import com.line7studio.boulderside.common.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetails implements SearchItemDetails {
    private String routeName;
    private Level level;
    private Long likeCount;
    private Long climberCount;
    private String boulderName;
}