package com.line7studio.boulderside.domain.aggregate.post.repository;

import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostSortType;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.line7studio.boulderside.domain.aggregate.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostsWithCursor(Long cursor, String subCursor, int size, PostType postType, PostSortType postSortType) {
        BooleanBuilder builder = new BooleanBuilder();

        // 게시글 유형 필터링
        if (postType != null) {
            builder.and(post.postType.eq(postType));
        }

        // 커서 기반 필터링 (stable pagination)
        if (cursor != null && subCursor != null && postSortType != null) {
            switch (postSortType) {
                case LATEST_CREATED -> {
                    LocalDateTime subCursorCreatedAt = LocalDateTime.parse(subCursor);
                    builder.and(
                        post.createdAt.lt(subCursorCreatedAt)
                            .or(post.createdAt.eq(subCursorCreatedAt).and(post.id.lt(cursor)))
                    );
                }
                case MOST_VIEWED -> {
                    Long subCursorViewCount = Long.parseLong(subCursor);
                    builder.and(
                        post.viewCount.lt(subCursorViewCount)
                            .or(post.viewCount.eq(subCursorViewCount).and(post.id.lt(cursor)))
                    );
                }
                case NEAREST_MEETING_DATE -> {
                    LocalDate subCursorMeetingDate = LocalDate.parse(subCursor);
                    builder.and(
                        post.meetingDate.gt(subCursorMeetingDate)
                            .or(post.meetingDate.eq(subCursorMeetingDate).and(post.id.gt(cursor)))
                    );
                }
            }
        }

        OrderSpecifier<?>[] orderSpecifiers = switch (Objects.requireNonNull(postSortType)) {
            case LATEST_CREATED -> new OrderSpecifier[]{post.createdAt.desc(), post.id.desc()};
            case MOST_VIEWED -> new OrderSpecifier[]{post.viewCount.desc(), post.id.desc()};
            case NEAREST_MEETING_DATE -> new OrderSpecifier[]{post.meetingDate.asc(), post.id.asc()};
        };

        return jpaQueryFactory
                .selectFrom(post)
                .where(builder)
                .orderBy(orderSpecifiers)
                .limit(size)
                .fetch();
    }
}
