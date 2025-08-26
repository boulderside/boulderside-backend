package com.line7studio.boulderside.domain.aggregate.comment.service;

import com.line7studio.boulderside.domain.aggregate.comment.entity.Comment;
import com.line7studio.boulderside.domain.aggregate.comment.enums.CommentDomainType;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsWithCursor(Long cursor, int size, Long domainId, CommentDomainType commentDomainType);
    Comment createComment(Long userId, Long domainId, CommentDomainType commentDomainType, String content);
    Comment updateComment(Long commentId, String content, Long userId);
    void deleteComment(Long commentId, Long userId);
}