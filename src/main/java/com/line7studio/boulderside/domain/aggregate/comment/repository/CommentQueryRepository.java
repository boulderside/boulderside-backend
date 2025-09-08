package com.line7studio.boulderside.domain.aggregate.comment.repository;

import com.line7studio.boulderside.domain.aggregate.comment.entity.Comment;
import com.line7studio.boulderside.domain.aggregate.comment.enums.CommentDomainType;

import java.util.List;
import java.util.Map;

public interface CommentQueryRepository {
    List<Comment> findCommentsWithCursor(Long cursor, int size, Long domainId, CommentDomainType commentDomainType);
    Map<Long, Long> countCommentsByDomainIdsAndType(List<Long> domainIds, CommentDomainType commentDomainType);
}