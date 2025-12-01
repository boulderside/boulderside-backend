package com.line7studio.boulderside.domain.feature.search.repository;

import com.line7studio.boulderside.application.search.dto.SearchItemResponse;
import com.line7studio.boulderside.application.search.dto.UnifiedSearchResponse;
import com.line7studio.boulderside.domain.feature.boulder.entity.QBoulder;
import com.line7studio.boulderside.domain.feature.image.entity.QImage;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.entity.QBoardPost;
import com.line7studio.boulderside.domain.feature.post.entity.QMatePost;
import com.line7studio.boulderside.domain.feature.region.entity.QRegion;
import com.line7studio.boulderside.domain.feature.route.QRoute;
import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.search.enums.DocumentDomainType;
import com.line7studio.boulderside.domain.feature.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {

    private static final QBoulder BOULDER = QBoulder.boulder;
    private static final QRoute ROUTE = QRoute.route;
    private static final QMatePost MATE_POST = QMatePost.matePost;
    private static final QBoardPost BOARD_POST = QBoardPost.boardPost;
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
            queryFactory.select(MATE_POST.title)
                .from(MATE_POST)
                .where(MATE_POST.title.containsIgnoreCase(keyword))
                .orderBy(MATE_POST.createdAt.desc(), MATE_POST.id.desc())
                .limit(limit)
                .fetch()
        );

        suggestions.addAll(
            queryFactory.select(BOARD_POST.title)
                .from(BOARD_POST)
                .where(BOARD_POST.title.containsIgnoreCase(keyword))
                .orderBy(BOARD_POST.createdAt.desc(), BOARD_POST.id.desc())
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
        List<PostSearchResult> mateResults = queryFactory
            .select(MATE_POST, USER.nickname)
            .from(MATE_POST)
            .leftJoin(USER).on(USER.id.eq(MATE_POST.userId))
            .where(MATE_POST.title.containsIgnoreCase(keyword))
            .orderBy(MATE_POST.createdAt.desc(), MATE_POST.id.desc())
            .limit(size)
            .fetch()
            .stream()
            .map(tuple -> {
                MatePost post = tuple.get(MATE_POST);
                String authorName = tuple.get(USER.nickname);
                SearchItemResponse response = SearchItemResponse.builder()
                    .id(String.valueOf(post.getId()))
                    .title(post.getTitle())
                    .domainType(DocumentDomainType.POST)
                    .authorName(authorName)
                    .viewCount(defaultLong(post.getViewCount()))
                    .commentCount(defaultLong(post.getCommentCount()))
                    .meetingDate(post.getMeetingDate())
                    .createdAt(post.getCreatedAt())
                    .build();
                return new PostSearchResult(response, post.getCreatedAt(), post.getId());
            })
            .toList();

        List<PostSearchResult> boardResults = queryFactory
            .select(BOARD_POST, USER.nickname)
            .from(BOARD_POST)
            .leftJoin(USER).on(USER.id.eq(BOARD_POST.userId))
            .where(BOARD_POST.title.containsIgnoreCase(keyword))
            .orderBy(BOARD_POST.createdAt.desc(), BOARD_POST.id.desc())
            .limit(size)
            .fetch()
            .stream()
            .map(tuple -> {
                BoardPost post = tuple.get(BOARD_POST);
                String authorName = tuple.get(USER.nickname);
                SearchItemResponse response = SearchItemResponse.builder()
                    .id(String.valueOf(post.getId()))
                    .title(post.getTitle())
                    .domainType(DocumentDomainType.POST)
                    .authorName(authorName)
                    .viewCount(defaultLong(post.getViewCount()))
                    .commentCount(defaultLong(post.getCommentCount()))
                    .createdAt(post.getCreatedAt())
                    .build();
                return new PostSearchResult(response, post.getCreatedAt(), post.getId());
            })
            .toList();

        return Stream.concat(mateResults.stream(), boardResults.stream())
            .sorted(Comparator.comparing(PostSearchResult::createdAt).reversed()
                .thenComparing(PostSearchResult::id, Comparator.reverseOrder()))
            .limit(size)
            .map(PostSearchResult::response)
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
        return countMatePosts(keyword) + countBoardPosts(keyword);
    }

    private long countMatePosts(String keyword) {
        Long count = queryFactory
            .select(MATE_POST.count())
            .from(MATE_POST)
            .where(MATE_POST.title.containsIgnoreCase(keyword))
            .fetchOne();
        return count != null ? count : 0L;
    }

    private long countBoardPosts(String keyword) {
        Long count = queryFactory
            .select(BOARD_POST.count())
            .from(BOARD_POST)
            .where(BOARD_POST.title.containsIgnoreCase(keyword))
            .fetchOne();
        return count != null ? count : 0L;
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
    private record PostSearchResult(SearchItemResponse response, LocalDateTime createdAt, Long id) {
    }
}
