package com.line7studio.boulderside.domain.aggregate.search.service;

import com.line7studio.boulderside.application.search.dto.SearchItemResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.enums.DocumentDomainType;

import java.util.List;

public interface SearchService {
    List<String> findSuggestionsWithKeyword(String keyword, int limit);
    UnifiedSearchResponse searchUnified(String keyword);
    List<SearchItemResponse> searchByDomain(String keyword, DocumentDomainType domain, int size);
    long countByDomain(String keyword, DocumentDomainType domain);
}