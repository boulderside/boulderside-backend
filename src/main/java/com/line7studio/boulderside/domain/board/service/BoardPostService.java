package com.line7studio.boulderside.domain.board.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.board.BoardPost;
import com.line7studio.boulderside.domain.board.enums.BoardPostSortType;
import com.line7studio.boulderside.domain.board.repository.BoardPostQueryRepository;
import com.line7studio.boulderside.domain.board.repository.BoardPostRepository;
import com.line7studio.boulderside.domain.enums.PostStatus;
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
        return boardPostRepository.findByIdAndStatus(postId, PostStatus.ACTIVE)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public List<BoardPost> getBoardPostsByIds(List<Long> postIds) {
        return boardPostRepository.findAllById(postIds);
    }

    public List<BoardPost> getBoardPostsWithCursor(Long cursor, String subCursor, int size, BoardPostSortType postSortType, List<Long> excludedUserIds) {
        if (postSortType == null) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }
        return boardPostQueryRepository.findBoardPostsWithCursor(cursor, subCursor, size, postSortType, true, excludedUserIds);
    }

    public List<BoardPost> getBoardPostsWithCursorForAdmin(Long cursor, String subCursor, int size, BoardPostSortType postSortType) {
        if (postSortType == null) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }
        return boardPostQueryRepository.findBoardPostsWithCursor(cursor, subCursor, size, postSortType, false, List.of());
    }

    public List<BoardPost> getBoardPostsByUser(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        if (cursor == null) {
            return boardPostRepository.findByUserIdAndStatusOrderByIdDesc(userId, PostStatus.ACTIVE, pageable);
        }
        return boardPostRepository.findByUserIdAndIdLessThanAndStatusOrderByIdDesc(userId, cursor, PostStatus.ACTIVE, pageable);
    }

    public BoardPost createBoardPost(Long userId, String title, String content) {
        // Entity의 정적 팩토리 메서드가 검증을 수행
        BoardPost boardPost = BoardPost.create(userId, title, content);
        return boardPostRepository.save(boardPost);
    }

    public BoardPost updateBoardPost(Long postId, String title, String content, Long userId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // Entity가 소유자 검증 및 업데이트 검증 수행
        boardPost.verifyOwner(userId);
        boardPost.update(title, content);

        return boardPostRepository.save(boardPost);
    }

    public void deleteBoardPost(Long postId, Long userId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // Entity가 소유자 검증 수행
        boardPost.verifyOwner(userId);
        boardPostRepository.deleteById(postId);
    }

    public BoardPost updateBoardPostAsAdmin(Long postId, String title, String content) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // Entity의 update가 검증 수행
        boardPost.update(title, content);
        return boardPostRepository.save(boardPost);
    }

    public void deleteBoardPostAsAdmin(Long postId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        boardPostRepository.delete(boardPost);
    }

    public void updateBoardPostStatus(Long postId, PostStatus status) {
        BoardPost boardPost = boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        boardPost.updateStatus(status);
        boardPostRepository.save(boardPost);
    }

    public BoardPost getBoardPostByIdForAdmin(Long postId) {
        return boardPostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public void updateBoardPostsStatusByUser(Long userId, PostStatus status) {
        List<BoardPost> posts = boardPostRepository.findAllByUserId(userId);
        posts.forEach(post -> post.updateStatus(status));
        boardPostRepository.saveAll(posts);
    }

    public void restoreBoardPostsStatusByUser(Long userId) {
        List<BoardPost> posts = boardPostRepository.findAllByUserId(userId);
        posts.forEach(BoardPost::activate);
        boardPostRepository.saveAll(posts);
    }
}
