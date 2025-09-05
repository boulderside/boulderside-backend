package com.line7studio.boulderside.application.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainSearchResponse {
    private List<SearchItemResponse> items;
    private long totalCount;
}