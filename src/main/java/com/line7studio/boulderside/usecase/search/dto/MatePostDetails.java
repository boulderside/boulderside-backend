package com.line7studio.boulderside.usecase.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatePostDetails implements SearchItemDetails {
    private String title;
    private String authorName;
    private Long commentCount;
    private Long viewCount;
    private LocalDate meetingDate;
}