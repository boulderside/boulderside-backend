package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.repository.MatePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatePostReadService {

    private final MatePostRepository matePostRepository;

    @Transactional
    public Long incrementCommentCount(Long postId) {
        MatePost matePost = matePostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        matePost.incrementCommentCount();
        return matePost.getCommentCount();
    }

    @Transactional
    public Long decrementCommentCount(Long postId) {
        MatePost matePost = matePostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        matePost.decrementCommentCount();
        return matePost.getCommentCount();
    }

    @Transactional(readOnly = true)
    public Long getCommentCount(Long postId) {
        return matePostRepository.findById(postId)
                .map(MatePost::getCommentCount)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }
}