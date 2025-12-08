package com.line7studio.boulderside.domain.feature.comment.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.comment.entity.Comment;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.feature.comment.repository.CommentQueryRepository;
import com.line7studio.boulderside.domain.feature.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public List<Comment> getCommentsWithCursor(Long cursor, int size, Long domainId, CommentDomainType commentDomainType) {
        return commentQueryRepository.findCommentsWithCursor(cursor, size, domainId, commentDomainType);
    }

    @Override
    public Comment createComment(Long userId, Long domainId, CommentDomainType commentDomainType, String content) {
        Comment comment = Comment.builder()
                .userId(userId)
                .domainId(domainId)
                .commentDomainType(commentDomainType)
                .content(content)
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long commentId, String content, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "댓글 수정은 작성자만 가능합니다.");
        }

        comment.update(content);

        return comment;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "댓글 삭제는 작성자만 가능합니다.");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public Long countCommentsByDomainIdAndCommentDomainType(Long domainId, CommentDomainType commentDomainType) {
        return commentRepository.countCommentsByDomainIdAndCommentDomainType(domainId, commentDomainType);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Override
    public Map<Long, Long> countCommentsByDomainIdsAndCommentDomainTypeType(List<Long> domainIds, CommentDomainType commentDomainType) {
        return commentQueryRepository.countCommentsByDomainIdsAndType(domainIds, commentDomainType);
    }

    @Override
    public Comment updateCommentAsAdmin(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
        comment.update(content);
        return comment;
    }

    @Override
    public void deleteCommentAsAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }
}
