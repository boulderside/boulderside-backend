package com.line7studio.boulderside.domain.mate.repository;

import com.line7studio.boulderside.domain.mate.MatePost;
import com.line7studio.boulderside.domain.mate.enums.MatePostSortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.line7studio.boulderside.domain.mate.QMatePost.matePost;

@Repository
@RequiredArgsConstructor
public class MatePostQueryRepositoryImpl implements MatePostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MatePost> findMatePostsWithCursor(Long cursor, String subCursor, int size, MatePostSortType postSortType) {
        BooleanBuilder builder = new BooleanBuilder();

        if (cursor != null && subCursor != null && postSortType != null) {
            switch (postSortType) {
                case LATEST_CREATED -> {
                    LocalDateTime subCursorCreatedAt = LocalDateTime.parse(subCursor);
                    builder.and(
                        matePost.createdAt.lt(subCursorCreatedAt)
                            .or(matePost.createdAt.eq(subCursorCreatedAt).and(matePost.id.lt(cursor)))
                    );
                }
                case MOST_VIEWED -> {
                    Long subCursorViewCount = Long.parseLong(subCursor);
                    builder.and(
                        matePost.viewCount.lt(subCursorViewCount)
                            .or(matePost.viewCount.eq(subCursorViewCount).and(matePost.id.lt(cursor)))
                    );
                }
                case NEAREST_MEETING_DATE -> {
                    LocalDate subCursorMeetingDate = LocalDate.parse(subCursor);
                    builder.and(
                        matePost.meetingDate.gt(subCursorMeetingDate)
                            .or(matePost.meetingDate.eq(subCursorMeetingDate).and(matePost.id.gt(cursor)))
                    );
                }
            }
        }

        OrderSpecifier<?>[] orderSpecifiers = switch (Objects.requireNonNull(postSortType)) {
            case LATEST_CREATED -> new OrderSpecifier[]{matePost.createdAt.desc(), matePost.id.desc()};
            case MOST_VIEWED -> new OrderSpecifier[]{matePost.viewCount.desc(), matePost.id.desc()};
            case NEAREST_MEETING_DATE -> new OrderSpecifier[]{matePost.meetingDate.asc(), matePost.id.asc()};
        };

        return jpaQueryFactory
            .selectFrom(matePost)
            .where(builder)
            .orderBy(orderSpecifiers)
            .limit(size)
            .fetch();
    }
}
