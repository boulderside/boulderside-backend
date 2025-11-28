package com.line7studio.boulderside.domain.aggregate.search.repository;

import com.line7studio.boulderside.application.search.dto.SearchItemResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.QBoulder;
import com.line7studio.boulderside.domain.aggregate.image.entity.QImage;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.post.entity.QPost;
import com.line7studio.boulderside.domain.aggregate.region.entity.QRegion;
import com.line7studio.boulderside.domain.aggregate.route.QRoute;
import com.line7studio.boulderside.domain.aggregate.route.Route;
import com.line7studio.boulderside.domain.aggregate.search.enums.DocumentDomainType;
import com.line7studio.boulderside.domain.aggregate.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {

    private static final QBoulder BOULDER = QBoulder.boulder;
    private static final QRoute ROUTE = QRoute.route;
    private static final QPost POST = QPost.post;
    private static final QRegion REGION = QRegion.region;
    private static final QImage IMAGE = QImage.image;
    private static final QUser USER = QUser.user;
    private static final int UNIFIED_DOMAIN_LIMIT = 4;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findSuggestionsWithKeyword(String keyword, int limit) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        List<String> suggestions = new ArrayList<>();

        suggestions.addAll(
            queryFactory.select(BOULDER.name)
                .from(BOULDER)
                .where(BOULDER.name.containsIgnoreCase(keyword))
                .orderBy(BOULDER.createdAt.desc(), BOULDER.id.desc())
                .limit(limit)
                .fetch()
        );

        suggestions.addAll(
            queryFactory.select(ROUTE.name)
                .from(ROUTE)
                .where(ROUTE.name.containsIgnoreCase(keyword))
                .orderBy(ROUTE.createdAt.desc(), ROUTE.id.desc())
                .limit(limit)
                .fetch()
        );

        suggestions.addAll(
            queryFactory.select(POST.title)
                .from(POST)
                .where(POST.title.containsIgnoreCase(keyword))
                .orderBy(POST.createdAt.desc(), POST.id.desc())
                .limit(limit)
                .fetch()
        );

        return suggestions.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .distinct()
            .limit(limit)
            .toList();
    }

    @Override
    public UnifiedSearchResponse searchUnified(String keyword) {
        Map<DocumentDomainType, UnifiedSearchResponse.DomainSearchResult> domainResults =
            new EnumMap<>(DocumentDomainType.class);
        Map<DocumentDomainType, Long> totalCounts = new EnumMap<>(DocumentDomainType.class);

        List<SearchItemResponse> boulderItems = fetchBoulderSearchItems(keyword, UNIFIED_DOMAIN_LIMIT);
        long boulderCount = countBoulders(keyword);
        domainResults.put(DocumentDomainType.BOULDER, UnifiedSearchResponse.DomainSearchResult.builder()
            .items(boulderItems)
            .hasMore(boulderCount > UNIFIED_DOMAIN_LIMIT)
            .build());
        totalCounts.put(DocumentDomainType.BOULDER, boulderCount);

        List<SearchItemResponse> routeItems = fetchRouteSearchItems(keyword, UNIFIED_DOMAIN_LIMIT);
        long routeCount = countRoutes(keyword);
        domainResults.put(DocumentDomainType.ROUTE, UnifiedSearchResponse.DomainSearchResult.builder()
            .items(routeItems)
            .hasMore(routeCount > UNIFIED_DOMAIN_LIMIT)
            .build());
        totalCounts.put(DocumentDomainType.ROUTE, routeCount);

        List<SearchItemResponse> postItems = fetchPostSearchItems(keyword, UNIFIED_DOMAIN_LIMIT);
        long postCount = countPosts(keyword);
        domainResults.put(DocumentDomainType.POST, UnifiedSearchResponse.DomainSearchResult.builder()
            .items(postItems)
            .hasMore(postCount > UNIFIED_DOMAIN_LIMIT)
            .build());
        totalCounts.put(DocumentDomainType.POST, postCount);

        return UnifiedSearchResponse.builder()
            .domainResults(domainResults)
            .totalCounts(totalCounts)
            .build();
    }

    @Override
    public List<SearchItemResponse> searchByDomain(String keyword, DocumentDomainType domain, int size) {
        return switch (domain) {
            case BOULDER -> fetchBoulderSearchItems(keyword, size);
            case ROUTE -> fetchRouteSearchItems(keyword, size);
            case POST -> fetchPostSearchItems(keyword, size);
        };
    }

    @Override
    public long countByDomain(String keyword, DocumentDomainType domain) {
        return switch (domain) {
            case BOULDER -> countBoulders(keyword);
            case ROUTE -> countRoutes(keyword);
            case POST -> countPosts(keyword);
        };
    }

    private List<SearchItemResponse> fetchBoulderSearchItems(String keyword, int size) {
        List<Tuple> tuples = queryFactory
            .select(BOULDER.id, BOULDER.name, BOULDER.likeCount, BOULDER.viewCount, BOULDER.createdAt,
                REGION.province, REGION.city, IMAGE.imageUrl)
            .from(BOULDER)
            .leftJoin(REGION).on(REGION.id.eq(BOULDER.regionId))
            .leftJoin(IMAGE).on(
                IMAGE.domainId.eq(BOULDER.id)
                    .and(IMAGE.imageDomainType.eq(ImageDomainType.BOULDER))
                    .and(IMAGE.orderIndex.eq(0))
            )
            .where(BOULDER.name.containsIgnoreCase(keyword))
            .orderBy(BOULDER.createdAt.desc(), BOULDER.id.desc())
            .limit(size)
            .fetch();

        return tuples.stream()
            .map(tuple -> SearchItemResponse.builder()
                .id(String.valueOf(tuple.get(BOULDER.id)))
                .title(tuple.get(BOULDER.name))
                .domainType(DocumentDomainType.BOULDER)
                .thumbnailUrl(tuple.get(IMAGE.imageUrl))
                .province(tuple.get(REGION.province))
                .city(tuple.get(REGION.city))
                .likeCount(defaultLong(tuple.get(BOULDER.likeCount)))
                .viewCount(defaultLong(tuple.get(BOULDER.viewCount)))
                .createdAt(tuple.get(BOULDER.createdAt))
                .build())
            .toList();
    }

    private List<SearchItemResponse> fetchRouteSearchItems(String keyword, int size) {
        List<Route> routes = queryFactory
            .selectFrom(ROUTE)
            .where(ROUTE.name.containsIgnoreCase(keyword))
            .orderBy(ROUTE.createdAt.desc(), ROUTE.id.desc())
            .limit(size)
            .fetch();

        return routes.stream()
            .map(route -> SearchItemResponse.builder()
                .id(String.valueOf(route.getId()))
                .title(route.getName())
                .domainType(DocumentDomainType.ROUTE)
                .level(route.getRouteLevel())
                .likeCount(defaultLong(route.getLikeCount()))
                .climberCount(defaultLong(route.getClimberCount()))
                .commentCount(defaultLong(route.getCommentCount()))
                .viewCount(defaultLong(route.getViewCount()))
                .createdAt(route.getCreatedAt())
                .build())
            .toList();
    }

    private List<SearchItemResponse> fetchPostSearchItems(String keyword, int size) {
        List<Tuple> tuples = queryFactory
            .select(POST, USER.nickname)
            .from(POST)
            .leftJoin(USER).on(USER.id.eq(POST.userId))
            .where(POST.title.containsIgnoreCase(keyword))
            .orderBy(POST.createdAt.desc(), POST.id.desc())
            .limit(size)
            .fetch();

        return tuples.stream()
            .map(tuple -> {
                Post post = tuple.get(POST);
                String authorName = tuple.get(USER.nickname);
                return SearchItemResponse.builder()
                    .id(String.valueOf(post.getId()))
                    .title(post.getTitle())
                    .domainType(DocumentDomainType.POST)
                    .authorName(authorName)
                    .viewCount(defaultLong(post.getViewCount()))
                    .commentCount(defaultLong(post.getCommentCount()))
                    .meetingDate(post.getMeetingDate())
                    .createdAt(post.getCreatedAt())
                    .build();
            })
            .toList();
    }

    private long countBoulders(String keyword) {
        Long count = queryFactory
            .select(BOULDER.count())
            .from(BOULDER)
            .where(BOULDER.name.containsIgnoreCase(keyword))
            .fetchOne();
        return count != null ? count : 0L;
    }

    private long countRoutes(String keyword) {
        Long count = queryFactory
            .select(ROUTE.count())
            .from(ROUTE)
            .where(ROUTE.name.containsIgnoreCase(keyword))
            .fetchOne();
        return count != null ? count : 0L;
    }

    private long countPosts(String keyword) {
        Long count = queryFactory
            .select(POST.count())
            .from(POST)
            .where(POST.title.containsIgnoreCase(keyword))
            .fetchOne();
        return count != null ? count : 0L;
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
}
