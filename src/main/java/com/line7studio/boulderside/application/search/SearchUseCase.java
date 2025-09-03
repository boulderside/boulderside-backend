package com.line7studio.boulderside.application.search;

import com.line7studio.boulderside.application.search.dto.AutocompleteResponse;
import com.line7studio.boulderside.application.search.dto.DomainSearchResponse;
import com.line7studio.boulderside.application.search.dto.SearchItemResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.aggregate.search.service.SearchService;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.enums.DocumentDomainType;
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

    public DomainSearchResponse searchByDomain(String keyword, DocumentDomainType domain, String cursor, int size) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return DomainSearchResponse.builder()
                    .items(List.of())
                    .nextCursor(null)
                    .hasMore(false)
                    .totalCount(0)
                    .build();
        }
        
        List<SearchItemResponse> items = searchService.searchByDomain(keyword, domain, cursor, size + 1);
        
        boolean hasMore = items.size() > size;
        if (hasMore) {
            items = items.subList(0, size);
        }
        
        String nextCursor = null;
        if (hasMore && !items.isEmpty()) {
            SearchItemResponse lastItem = items.getLast();
            nextCursor = lastItem.getCreatedAt().toString();
        }
        
        long totalCount = searchService.countByDomain(keyword, domain);
        
        return DomainSearchResponse.builder()
                .items(items)
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .totalCount(totalCount)
                .build();
    }
}