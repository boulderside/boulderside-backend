package com.line7studio.boulderside.domain.post.repository;

import com.line7studio.boulderside.domain.post.entity.BoardPost;
import com.line7studio.boulderside.domain.post.enums.BoardPostSortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.line7studio.boulderside.domain.feature.post.entity.QBoardPost.boardPost;

@Repository
@RequiredArgsConstructor
public class BoardPostQueryRepositoryImpl implements BoardPostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BoardPost> findBoardPostsWithCursor(Long cursor, String subCursor, int size, BoardPostSortType postSortType) {
        BooleanBuilder builder = new BooleanBuilder();

        if (cursor != null && subCursor != null && postSortType != null) {
            switch (postSortType) {
                case LATEST_CREATED -> {
                    LocalDateTime subCursorCreatedAt = LocalDateTime.parse(subCursor);
                    builder.and(
                        boardPost.createdAt.lt(subCursorCreatedAt)
                            .or(boardPost.createdAt.eq(subCursorCreatedAt).and(boardPost.id.lt(cursor)))
                    );
                }
                case MOST_VIEWED -> {
                    Long subCursorViewCount = Long.parseLong(subCursor);
                    builder.and(
                        boardPost.viewCount.lt(subCursorViewCount)
                            .or(boardPost.viewCount.eq(subCursorViewCount).and(boardPost.id.lt(cursor)))
                    );
                }
            }
        }

        OrderSpecifier<?>[] orderSpecifiers = switch (Objects.requireNonNull(postSortType)) {
            case LATEST_CREATED -> new OrderSpecifier[]{boardPost.createdAt.desc(), boardPost.id.desc()};
            case MOST_VIEWED -> new OrderSpecifier[]{boardPost.viewCount.desc(), boardPost.id.desc()};
        };

        return jpaQueryFactory
            .selectFrom(boardPost)
            .where(builder)
            .orderBy(orderSpecifiers)
            .limit(size)
            .fetch();
    }
}
