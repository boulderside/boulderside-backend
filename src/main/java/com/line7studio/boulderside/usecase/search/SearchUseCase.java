package com.line7studio.boulderside.usecase.search;

import com.line7studio.boulderside.usecase.search.dto.AutocompleteResponse;
import com.line7studio.boulderside.usecase.search.dto.DomainSearchResponse;
import com.line7studio.boulderside.usecase.search.dto.SearchItemResponse;
import com.line7studio.boulderside.usecase.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.search.service.SearchService;
import com.line7studio.boulderside.domain.search.enums.DocumentDomainType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchUseCase {
    
    private final SearchService searchService;
    private static final int AUTOCOMPLETE_LIMIT = 10;
    private static final int MAX_PAGE_SIZE = 50;

    public AutocompleteResponse getSuggestions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return AutocompleteResponse.builder()
                    .suggestionList(List.of())
                    .build();
        }

        List<String> suggestionList = searchService.findSuggestionsWithKeyword(keyword, AUTOCOMPLETE_LIMIT);
        return AutocompleteResponse.builder()
                .suggestionList(suggestionList)
                .build();
    }

    public UnifiedSearchResponse searchUnified(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return UnifiedSearchResponse.builder()
                    .domainResults(Map.of())
                    .totalCounts(Map.of())
                    .build();
        }
        
        return searchService.searchUnified(keyword);
    }

    public DomainSearchResponse searchByDomain(String keyword, DocumentDomainType domain, int size) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return DomainSearchResponse.builder()
                    .items(List.of())
                    .totalCount(0)
                    .build();
        }
        int pageSize = normalizeSize(size);
        List<SearchItemResponse> items = searchService.searchByDomain(keyword, domain, pageSize);
        long totalCount = searchService.countByDomain(keyword, domain);
        
        return DomainSearchResponse.builder()
                .items(items)
                .totalCount(totalCount)
                .build();
    }

    private int normalizeSize(int size) {
        if (size <= 0) {
            return 10;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }
}
