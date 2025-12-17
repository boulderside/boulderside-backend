package com.line7studio.boulderside.domain.search.service;

import com.line7studio.boulderside.usecase.search.dto.SearchItemResponse;
import com.line7studio.boulderside.usecase.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.search.repository.SearchRepository;
import com.line7studio.boulderside.domain.search.enums.DocumentDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    
    private final SearchRepository searchRepository;

    public List<String> findSuggestionsWithKeyword(String keyword, int limit) {
        return searchRepository.findSuggestionsWithKeyword(keyword, limit);
    }

    public UnifiedSearchResponse searchUnified(String keyword) {
        return searchRepository.searchUnified(keyword);
    }

    public List<SearchItemResponse> searchByDomain(String keyword, DocumentDomainType domain, int size) {
        return searchRepository.searchByDomain(keyword, domain, size);
    }

    public long countByDomain(String keyword, DocumentDomainType domain) {
        return searchRepository.countByDomain(keyword, domain);
    }
}
