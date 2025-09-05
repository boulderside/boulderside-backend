package com.line7studio.boulderside.infrastructure.elasticsearch.repository;

import com.line7studio.boulderside.application.search.dto.SearchItemResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.BoulderDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.PostDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.RouteDocument;
import com.line7studio.boulderside.infrastructure.elasticsearch.document.enums.DocumentDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements ElasticsearchSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<String> findSuggestionsWithKeyword(String keyword, int limit) {

        String boulderQueryString = """
            {
                "match": {
                    "boulderName": "%s"
                }
            }
            """.formatted(keyword);

        StringQuery boulderQuery = new StringQuery(boulderQueryString);
        SearchHits<BoulderDocument> boulderHits = elasticsearchOperations.search(boulderQuery, BoulderDocument.class);
        List<String> suggestions = new ArrayList<>(boulderHits.stream()
                .map(SearchHit::getContent)
                .map(BoulderDocument::getBoulderName)
                .toList());

        String routeQueryString = """
            {
                "match": {
                    "routeName": "%s"
                }
            }
            """.formatted(keyword);

        StringQuery routeQuery = new StringQuery(routeQueryString);
        SearchHits<RouteDocument> routeHits = elasticsearchOperations.search(routeQuery, RouteDocument.class);
        suggestions.addAll(routeHits.stream()
                .map(SearchHit::getContent)
                .map(RouteDocument::getRouteName)
                .toList());

        String postQueryString = """
            {
                "match": {
                    "title": "%s"
                }
            }
            """.formatted(keyword);

        StringQuery postQuery = new StringQuery(postQueryString);
        SearchHits<PostDocument> postHits = elasticsearchOperations.search(postQuery, PostDocument.class);
        suggestions.addAll(postHits.stream()
                .map(SearchHit::getContent)
                .map(PostDocument::getTitle)
                .toList());

        return suggestions.stream()
                .distinct()
                .limit(limit)
                .toList();
    }

    @Override
    public UnifiedSearchResponse searchUnified(String keyword) {
        Map<DocumentDomainType, UnifiedSearchResponse.DomainSearchResult> domainResults =
                new EnumMap<>(DocumentDomainType.class);

        Map<DocumentDomainType, Long> totalCounts =
                new EnumMap<>(DocumentDomainType.class);

        String boulderQueryString = """
            {
                "multi_match": {
                    "query": "%s",
                    "fields": ["boulderName"]
                }
            }
            """.formatted(keyword);

        StringQuery boulderQuery = new StringQuery(boulderQueryString);
        boulderQuery.setPageable(org.springframework.data.domain.PageRequest.of(0, 4));
        SearchHits<BoulderDocument> boulderHits = elasticsearchOperations.search(boulderQuery, BoulderDocument.class);
        List<SearchItemResponse> boulderItems = boulderHits.stream()
                .limit(4)
                .map(SearchHit::getContent)
                .map(this::convertBoulderToSearchItem)
                .toList();

        boolean boulderHasMore = boulderHits.getTotalHits() > 3;
        domainResults.put(DocumentDomainType.BOULDER, UnifiedSearchResponse.DomainSearchResult.builder()
                .items(boulderItems)
                .hasMore(boulderHasMore)
                .build());
        totalCounts.put(DocumentDomainType.BOULDER, boulderHits.getTotalHits());

        String routeQueryString = """
            {
                "multi_match": {
                    "query": "%s",
                    "fields": ["routeName"]
                }
            }
            """.formatted(keyword);

        StringQuery routeQuery = new StringQuery(routeQueryString);
        routeQuery.setPageable(org.springframework.data.domain.PageRequest.of(0, 4));
        SearchHits<RouteDocument> routeHits = elasticsearchOperations.search(routeQuery, RouteDocument.class);
        List<SearchItemResponse> routeItems = routeHits.stream()
                .limit(4)
                .map(SearchHit::getContent)
                .map(this::convertRouteToSearchItem)
                .toList();

        boolean routeHasMore = routeHits.getTotalHits() > 3;
        domainResults.put(DocumentDomainType.ROUTE, UnifiedSearchResponse.DomainSearchResult.builder()
                .items(routeItems)
                .hasMore(routeHasMore)
                .build());
        totalCounts.put(DocumentDomainType.ROUTE, routeHits.getTotalHits());

        String postQueryString = """
            {
                "multi_match": {
                    "query": "%s",
                    "fields": ["title"]
                }
            }
            """.formatted(keyword);

        StringQuery postQuery = new StringQuery(postQueryString);
        postQuery.setPageable(org.springframework.data.domain.PageRequest.of(0, 4));
        SearchHits<PostDocument> postHits = elasticsearchOperations.search(postQuery, PostDocument.class);
        List<SearchItemResponse> postItems = postHits.stream()
                .limit(4)
                .map(SearchHit::getContent)
                .map(this::convertPostToSearchItem)
                .toList();

        boolean postHasMore = postHits.getTotalHits() > 3;
        domainResults.put(DocumentDomainType.POST, UnifiedSearchResponse.DomainSearchResult.builder()
                .items(postItems)
                .hasMore(postHasMore)
                .build());
        totalCounts.put(DocumentDomainType.POST, postHits.getTotalHits());

        return UnifiedSearchResponse.builder()
                .domainResults(domainResults)
                .totalCounts(totalCounts)
                .build();
    }

    @Override
    public List<SearchItemResponse> searchByDomain(String keyword, DocumentDomainType domain, int size) {
        String searchField = getSearchField(domain);
        Class<?> documentClass = getDocumentClass(domain);

        String queryString = """
            {
                "multi_match": {
                    "query": "%s",
                    "fields": ["%s"]
                }
            }
            """.formatted(keyword, searchField);

        StringQuery query = new StringQuery(queryString);
        query.setPageable(org.springframework.data.domain.PageRequest.of(0, size));
        SearchHits<?> hits = elasticsearchOperations.search(query, documentClass);

        return hits.stream()
                .map(SearchHit::getContent)
                .map(content -> convertToSearchItem(content, domain))
                .toList();
    }

    @Override
    public long countByDomain(String keyword, DocumentDomainType domain) {
        String searchField = getSearchField(domain);
        Class<?> documentClass = getDocumentClass(domain);

        String queryString = """
            {
                "multi_match": {
                    "query": "%s",
                    "fields": ["%s"]
                }
            }
            """.formatted(keyword, searchField);

        StringQuery query = new StringQuery(queryString);
        return elasticsearchOperations.count(query, documentClass);
    }

    private String getSearchField(DocumentDomainType domain) {
        return switch (domain) {
            case BOULDER -> "boulderName";
            case ROUTE -> "routeName";
            case POST -> "title";
        };
    }

    private Class<?> getDocumentClass(DocumentDomainType domain) {
        return switch (domain) {
            case BOULDER -> BoulderDocument.class;
            case ROUTE -> RouteDocument.class;
            case POST -> PostDocument.class;
        };
    }

    private SearchItemResponse convertToSearchItem(Object document, DocumentDomainType domain) {
        return switch (domain) {
            case BOULDER -> convertBoulderToSearchItem((BoulderDocument) document);
            case ROUTE -> convertRouteToSearchItem((RouteDocument) document);
            case POST -> convertPostToSearchItem((PostDocument) document);
        };
    }

    private SearchItemResponse convertBoulderToSearchItem(BoulderDocument boulder) {
        return SearchItemResponse.builder()
                .id(boulder.getId())
                .title(boulder.getBoulderName())
                .domainType(DocumentDomainType.BOULDER)
                .thumbnailUrl(boulder.getThumbnailUrl())
                .province(boulder.getProvince())
                .city(boulder.getCity())
                .createdAt(boulder.getCreatedAt())
                .build();
    }

    private SearchItemResponse convertRouteToSearchItem(RouteDocument route) {
        return SearchItemResponse.builder()
                .id(route.getId())
                .title(route.getRouteName())
                .domainType(DocumentDomainType.ROUTE)
                .level(route.getLevel())
                .likeCount(route.getLikeCount())
                .climberCount(route.getClimberCount())
                .createdAt(route.getCreatedAt())
                .build();
    }

    private SearchItemResponse convertPostToSearchItem(PostDocument post) {
        return SearchItemResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .domainType(DocumentDomainType.POST)
                .authorName(post.getAuthorName())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .meetingDate(post.getMeetingDate())
                .createdAt(post.getCreatedAt())
                .build();
    }
}