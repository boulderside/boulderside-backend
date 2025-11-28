package com.line7studio.boulderside.domain.aggregate.boulder.repository;

import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.enums.BoulderSortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.line7studio.boulderside.domain.aggregate.boulder.entity.QBoulder.boulder;

@Repository
@RequiredArgsConstructor
public class BoulderQueryRepositoryImpl implements BoulderQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Boulder> findBouldersWithCursor(BoulderSortType sortType, Long cursor, String subCursor, int size) {
		BooleanBuilder builder = new BooleanBuilder();

		if (cursor != null && subCursor != null && sortType != null) {
            if (sortType == BoulderSortType.LATEST_CREATED) {
                LocalDateTime subCursorCreatedAt = LocalDateTime.parse(subCursor);
                builder.and(
                        boulder.createdAt.lt(subCursorCreatedAt)
                                .or(boulder.createdAt.eq(subCursorCreatedAt).and(boulder.id.lt(cursor)))
                );
            } else if (sortType == BoulderSortType.MOST_LIKED) {
                Long subCursorLikeCount = Long.parseLong(subCursor);
                builder.and(
                        boulder.likeCount.lt(subCursorLikeCount)
                                .or(boulder.likeCount.eq(subCursorLikeCount).and(boulder.id.lt(cursor)))
                );
            }
		}

		OrderSpecifier<?>[] orderSpecifiers = switch (Objects.requireNonNull(sortType)) {
			case LATEST_CREATED -> new OrderSpecifier[]{boulder.createdAt.desc(), boulder.id.desc()};
			case MOST_LIKED -> new OrderSpecifier[]{boulder.likeCount.desc(), boulder.id.desc()};
		};

		return jpaQueryFactory
				.selectFrom(boulder)
				.where(builder)
				.orderBy(orderSpecifiers)
				.limit(size)
				.fetch();
	}
}