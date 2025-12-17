package com.line7studio.boulderside.domain.feature.comment.repository;

import com.line7studio.boulderside.domain.feature.comment.Comment;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countCommentsByDomainIdAndCommentDomainType(Long domainId, CommentDomainType commentDomainType);
}