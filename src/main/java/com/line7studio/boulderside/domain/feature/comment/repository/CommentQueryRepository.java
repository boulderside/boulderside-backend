package com.line7studio.boulderside.domain.feature.comment.repository;

import com.line7studio.boulderside.domain.feature.comment.entity.Comment;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;

import java.util.List;
import java.util.Map;

public interface CommentQueryRepository {
    List<Comment> findCommentsWithCursor(Long cursor, int size, Long domainId, CommentDomainType commentDomainType);
    List<Comment> findCommentsByUserWithCursor(Long cursor, int size, Long userId);
    List<Comment> findCommentsByUserAndTypeWithCursor(Long cursor, int size, Long userId, CommentDomainType commentDomainType);
    Map<Long, Long> countCommentsByDomainIdsAndType(List<Long> domainIds, CommentDomainType commentDomainType);
}
