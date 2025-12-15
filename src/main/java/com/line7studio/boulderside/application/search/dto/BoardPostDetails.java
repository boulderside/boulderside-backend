package com.line7studio.boulderside.application.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostDetails implements SearchItemDetails {
    private String title;
    private String authorName;
    private Long commentCount;
    private Long viewCount;
}