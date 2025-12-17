package com.line7studio.boulderside.usecase.search.dto;

import com.line7studio.boulderside.domain.feature.search.enums.DocumentDomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemResponse {
    private String id;
    private DocumentDomainType domainType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SearchItemDetails details;
}
