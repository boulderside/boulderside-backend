package com.line7studio.boulderside.domain.aggregate.search.service;

import com.line7studio.boulderside.application.search.dto.SearchItemResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.aggregate.search.repository.SearchRepository;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.enums.DocumentDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    
    private final SearchRepository searchRepository;

    @Override
    public List<String> findSuggestionsWithKeyword(String keyword, int limit) {
        return searchRepository.findSuggestionsWithKeyword(keyword, limit);
    }

    @Override
    public UnifiedSearchResponse searchUnified(String keyword) {
        return searchRepository.searchUnified(keyword);
    }

    @Override
    public List<SearchItemResponse> searchByDomain(String keyword, DocumentDomainType domain, String cursor, int size) {
        return searchRepository.searchByDomain(keyword, domain, cursor, size);
    }

    @Override
    public long countByDomain(String keyword, DocumentDomainType domain) {
        return searchRepository.countByDomain(keyword, domain);
    }
}