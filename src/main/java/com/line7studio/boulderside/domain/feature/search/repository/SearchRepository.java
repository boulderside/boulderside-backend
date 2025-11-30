package com.line7studio.boulderside.domain.feature.search.repository;

import com.line7studio.boulderside.application.search.dto.SearchItemResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.feature.search.enums.DocumentDomainType;

import java.util.List;

public interface SearchRepository {
    List<String> findSuggestionsWithKeyword(String keyword, int limit);
    UnifiedSearchResponse searchUnified(String keyword);
    List<SearchItemResponse> searchByDomain(String keyword, DocumentDomainType domain, int size);
    long countByDomain(String keyword, DocumentDomainType domain);
}
