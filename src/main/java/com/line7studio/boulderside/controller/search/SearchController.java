package com.line7studio.boulderside.controller.search;

import com.line7studio.boulderside.application.search.SearchUseCase;
import com.line7studio.boulderside.application.search.dto.AutocompleteResponse;
import com.line7studio.boulderside.application.search.dto.DomainSearchResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.domain.aggregate.search.enums.DocumentDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final SearchUseCase searchUseCase;

    @GetMapping("/suggest")
    public ResponseEntity<ApiResponse<AutocompleteResponse>> getAutocompleteSuggestions(
            @RequestParam String keyword) {
        AutocompleteResponse response = searchUseCase.getSuggestions(keyword);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/unified")
    public ResponseEntity<ApiResponse<UnifiedSearchResponse>> searchUnified(
            @RequestParam String keyword) {
        UnifiedSearchResponse response = searchUseCase.searchUnified(keyword);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/domain")
    public ResponseEntity<ApiResponse<DomainSearchResponse>> searchByDomain(
            @RequestParam String keyword,
            @RequestParam DocumentDomainType domain,
            @RequestParam(defaultValue = "10") int size) {
        DomainSearchResponse response = searchUseCase.searchByDomain(keyword, domain, size);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
