package com.line7studio.boulderside.domain.search.repository;

import com.line7studio.boulderside.usecase.search.dto.SearchItemResponse;
import com.line7studio.boulderside.usecase.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.search.enums.DocumentDomainType;

import java.util.List;

public interface SearchRepository {
    List<String> findSuggestionsWithKeyword(String keyword, int limit);
    UnifiedSearchResponse searchUnified(String keyword);
    List<SearchItemResponse> searchByDomain(String keyword, DocumentDomainType domain, int size);
    long countByDomain(String keyword, DocumentDomainType domain);
}
