package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import com.line7studio.boulderside.domain.feature.post.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardPostReadService {

    private final BoardPostRepository boardPostRepository;

    @Transactional
    public Long incrementCommentCount(Long postId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        boardPost.incrementCommentCount();
        return boardPost.getCommentCount();
    }

    @Transactional
    public Long decrementCommentCount(Long postId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        boardPost.decrementCommentCount();
        return boardPost.getCommentCount();
    }

    @Transactional(readOnly = true)
    public Long getCommentCount(Long postId) {
        return boardPostRepository.findById(postId)
                .map(BoardPost::getCommentCount)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }
}