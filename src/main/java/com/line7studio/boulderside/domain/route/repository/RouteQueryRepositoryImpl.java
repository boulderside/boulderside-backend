package com.line7studio.boulderside.domain.route.repository;

import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.enums.RouteSortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.line7studio.boulderside.domain.route.QRoute.route;

@Repository
@RequiredArgsConstructor
public class RouteQueryRepositoryImpl implements RouteQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Route> findRoutesWithCursor(RouteSortType sortType, Long cursor, String subCursor, int size) {
		BooleanBuilder builder = new BooleanBuilder();

		if (cursor != null && subCursor != null && sortType != null) {
            if (sortType == RouteSortType.DIFFICULTY) {
                String subCursorLevel = subCursor;
                builder.and(
                        route.routeLevel.stringValue().gt(subCursorLevel)
                                .or(route.routeLevel.stringValue().eq(subCursorLevel).and(route.id.lt(cursor)))
                );
            } else if (sortType == RouteSortType.MOST_LIKED) {
                Long subCursorLikeCount = Long.parseLong(subCursor);
                builder.and(
                        route.likeCount.lt(subCursorLikeCount)
                                .or(route.likeCount.eq(subCursorLikeCount).and(route.id.lt(cursor)))
                );
            } else if (sortType == RouteSortType.MOST_CLIMBERS) {
                Long subCursorClimberCount = Long.parseLong(subCursor);
                builder.and(
                        route.climberCount.lt(subCursorClimberCount)
                                .or(route.climberCount.eq(subCursorClimberCount).and(route.id.lt(cursor)))
                );
            }
		}

		OrderSpecifier<?>[] orderSpecifiers = switch (Objects.requireNonNull(sortType)) {
			case DIFFICULTY -> new OrderSpecifier[]{route.routeLevel.asc(), route.id.desc()};
            case MOST_LIKED -> new OrderSpecifier[]{route.likeCount.desc(), route.id.desc()};
            case MOST_CLIMBERS -> new OrderSpecifier[]{route.climberCount.desc(), route.id.desc()};
		};

		return jpaQueryFactory
				.selectFrom(route)
				.where(builder)
				.orderBy(orderSpecifiers)
				.limit(size)
				.fetch();
	}
}