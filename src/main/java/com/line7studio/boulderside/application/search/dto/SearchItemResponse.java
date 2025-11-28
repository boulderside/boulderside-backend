package com.line7studio.boulderside.application.search.dto;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.aggregate.search.enums.DocumentDomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemResponse {
    private String id;
    private String title;
    private DocumentDomainType domainType;
    private String thumbnailUrl;
    private String province;
    private String city;
    private Level level;
    private String authorName;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    private Long climberCount;
    private LocalDate meetingDate;
    private LocalDateTime createdAt;
}
