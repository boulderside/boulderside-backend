package com.line7studio.boulderside.domain.comment.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.comment.Comment;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.comment.repository.CommentQueryRepository;
import com.line7studio.boulderside.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;

    public List<Comment> getCommentsWithCursor(Long cursor, int size, Long domainId, CommentDomainType commentDomainType) {
        return commentQueryRepository.findCommentsWithCursor(cursor, size, domainId, commentDomainType);
    }

    public List<Comment> getCommentsByUserWithCursor(Long cursor, int size, Long userId) {
        return commentQueryRepository.findCommentsByUserWithCursor(cursor, size, userId);
    }

    public List<Comment> getCommentsByUserAndTypeWithCursor(Long cursor, int size, Long userId, CommentDomainType commentDomainType) {
        return commentQueryRepository.findCommentsByUserAndTypeWithCursor(cursor, size, userId, commentDomainType);
    }

    public Comment createComment(Long userId, Long domainId, CommentDomainType commentDomainType, String content) {
        Comment comment = Comment.builder()
                .userId(userId)
                .domainId(domainId)
                .commentDomainType(commentDomainType)
                .content(content)
                .build();

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, String content, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "댓글 수정은 작성자만 가능합니다.");
        }

        comment.update(content);

        return comment;
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "댓글 삭제는 작성자만 가능합니다.");
        }

        commentRepository.deleteById(commentId);
    }

    public Long countCommentsByDomainIdAndCommentDomainType(Long domainId, CommentDomainType commentDomainType) {
        return commentRepository.countCommentsByDomainIdAndCommentDomainType(domainId, commentDomainType);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public Map<Long, Long> countCommentsByDomainIdsAndCommentDomainTypeType(List<Long> domainIds, CommentDomainType commentDomainType) {
        return commentQueryRepository.countCommentsByDomainIdsAndType(domainIds, commentDomainType);
    }

    public Comment updateCommentAsAdmin(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
        comment.update(content);
        return comment;
    }

    public void deleteCommentAsAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }
}
