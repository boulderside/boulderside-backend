package com.line7studio.boulderside.domain.comment.repository;

import com.line7studio.boulderside.domain.comment.Comment;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countCommentsByDomainIdAndCommentDomainTypeAndStatus(Long domainId, CommentDomainType commentDomainType, PostStatus status);

    java.util.List<Comment> findAllByUserId(Long userId);
}
