package com.line7studio.boulderside.usecase.search.dto;

import com.line7studio.boulderside.domain.search.enums.DocumentDomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedSearchResponse {
    private Map<DocumentDomainType, DomainSearchResult> domainResults;
    private Map<DocumentDomainType, Long> totalCounts;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomainSearchResult {
        private List<SearchItemResponse> items;
        private boolean hasMore;
    }
}
