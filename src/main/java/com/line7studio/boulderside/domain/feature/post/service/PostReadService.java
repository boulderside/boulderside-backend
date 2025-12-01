package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.repository.BoardPostRepository;
import com.line7studio.boulderside.domain.feature.post.repository.MatePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final MatePostRepository matePostRepository;
    private final BoardPostRepository boardPostRepository;

    public void incrementCommentCount(Long postId) {
        findPost(postId).incrementCommentCount();
    }

    public void decrementCommentCount(Long postId) {
        findPost(postId).decrementCommentCount();
    }

    private CommentCountUpdatable findPost(Long postId) {
        return matePostRepository.findById(postId)
            .<CommentCountUpdatable>map(MatePostWrapper::new)
            .orElseGet(() -> boardPostRepository.findById(postId)
                .<CommentCountUpdatable>map(BoardPostWrapper::new)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND)));
    }

    private interface CommentCountUpdatable {
        void incrementCommentCount();

        void decrementCommentCount();
    }

    private static class MatePostWrapper implements CommentCountUpdatable {
        private final MatePost matePost;

        private MatePostWrapper(MatePost matePost) {
            this.matePost = matePost;
        }

        @Override
        public void incrementCommentCount() {
            matePost.incrementCommentCount();
        }

        @Override
        public void decrementCommentCount() {
            matePost.decrementCommentCount();
        }
    }

    private static class BoardPostWrapper implements CommentCountUpdatable {
        private final BoardPost boardPost;

        private BoardPostWrapper(BoardPost boardPost) {
            this.boardPost = boardPost;
        }

        @Override
        public void incrementCommentCount() {
            boardPost.incrementCommentCount();
        }

        @Override
        public void decrementCommentCount() {
            boardPost.decrementCommentCount();
        }
    }
}
