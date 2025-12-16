package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import com.line7studio.boulderside.domain.feature.post.enums.BoardPostSortType;
import com.line7studio.boulderside.domain.feature.post.repository.BoardPostQueryRepository;
import com.line7studio.boulderside.domain.feature.post.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardPostService {

    private final BoardPostRepository boardPostRepository;
    private final BoardPostQueryRepository boardPostQueryRepository;

    public BoardPost getBoardPostById(Long postId) {
        return boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public List<BoardPost> getBoardPostsByIds(List<Long> postIds) {
        return boardPostRepository.findAllById(postIds);
    }

    public List<BoardPost> getBoardPostsWithCursor(Long cursor, String subCursor, int size, BoardPostSortType postSortType) {
        if (postSortType == null) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }
        return boardPostQueryRepository.findBoardPostsWithCursor(cursor, subCursor, size, postSortType);
    }

    public List<BoardPost> getBoardPostsByUser(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        if (cursor == null) {
            return boardPostRepository.findByUserIdOrderByIdDesc(userId, pageable);
        }
        return boardPostRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
    }

    public BoardPost createBoardPost(Long userId, String title, String content) {
        BoardPost boardPost = BoardPost.create(userId, title, content);
        return boardPostRepository.save(boardPost);
    }

    public BoardPost updateBoardPost(Long postId, String title, String content, Long userId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        validateOwner(boardPost.getUserId(), userId);
        boardPost.update(title, content);
        return boardPostRepository.save(boardPost);
    }

    public void deleteBoardPost(Long postId, Long userId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        validateOwner(boardPost.getUserId(), userId);
        boardPostRepository.deleteById(postId);
    }

    public BoardPost updateBoardPostAsAdmin(Long postId, String title, String content) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        boardPost.update(title, content);
        return boardPostRepository.save(boardPost);
    }

    public void deleteBoardPostAsAdmin(Long postId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        boardPostRepository.delete(boardPost);
    }

    private void validateOwner(Long ownerId, Long userId) {
        if (!ownerId.equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "게시글 작업은 작성자만 가능합니다.");
        }
    }
}
