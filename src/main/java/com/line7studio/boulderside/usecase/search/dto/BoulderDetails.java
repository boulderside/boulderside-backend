package com.line7studio.boulderside.usecase.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoulderDetails implements SearchItemDetails {
    private String thumbnailUrl;
    private String province;
    private String city;
    private Long likeCount;
    private Long viewCount;
    private String boulderName;
}