package com.line7studio.boulderside.domain.aggregate.comment.repository;

import com.line7studio.boulderside.domain.aggregate.comment.entity.Comment;
import com.line7studio.boulderside.domain.aggregate.comment.enums.CommentDomainType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.line7studio.boulderside.domain.aggregate.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findCommentsWithCursor(Long cursor, int size, Long domainId, CommentDomainType commentDomainType) {
        BooleanBuilder builder = new BooleanBuilder();

        // 도메인 ID 필터링
        if (domainId != null) {
            builder.and(comment.domainId.eq(domainId));
        }

        // 도메인 타입 필터링
        if (commentDomainType != null) {
            builder.and(comment.commentDomainType.eq(commentDomainType));
        }

        // 커서 기반 페이징 (등록순)
        if (cursor != null) {
            builder.and(comment.id.gt(cursor));
        }

        return jpaQueryFactory
                .selectFrom(comment)
                .where(builder)
                .orderBy(comment.id.asc())
                .limit(size)
                .fetch();
    }
}