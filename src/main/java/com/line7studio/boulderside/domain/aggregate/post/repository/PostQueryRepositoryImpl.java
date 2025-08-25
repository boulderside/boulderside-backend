package com.line7studio.boulderside.domain.aggregate.post.repository;

import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostSortType;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static com.line7studio.boulderside.domain.aggregate.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostsWithCursor(Long cursor, int size, PostType postType, PostSortType postSortType) {
        BooleanBuilder builder = new BooleanBuilder();

        // 게시글 유형 필터링
        if (postType != null) {
            builder.and(post.postType.eq(postType));
        }

        // 커서 번호 필터링
        if (cursor != null && postSortType != null) {
            switch (postSortType) {
                case LATEST_CREATED -> builder.and(post.id.lt(cursor));
                case MOST_VIEWED -> builder.and(post.viewCount.lt(cursor.intValue()));
                case NEAREST_MEETING_DATE -> {
                    LocalDate cursorDate = Instant.ofEpochSecond(cursor)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate();
                    builder.and(post.meetingDate.gt(cursorDate));
                }
            }
        }

        OrderSpecifier<?> orderSpecifier = switch (Objects.requireNonNull(postSortType)) {
            case LATEST_CREATED -> post.createdAt.desc();
            case MOST_VIEWED -> post.viewCount.desc();
            case NEAREST_MEETING_DATE -> post.meetingDate.asc();
        };

        return jpaQueryFactory
                .selectFrom(post)
                .where(builder)
                .orderBy(orderSpecifier)
                .limit(size)
                .fetch();
    }
}
