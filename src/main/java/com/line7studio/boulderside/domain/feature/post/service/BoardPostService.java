package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import com.line7studio.boulderside.domain.feature.post.enums.BoardPostSortType;

import java.util.List;

public interface BoardPostService {
    BoardPost getBoardPostById(Long postId);

    List<BoardPost> getBoardPostsWithCursor(Long cursor, String subCursor, int size, BoardPostSortType postSortType);

    List<BoardPost> getBoardPostsByUser(Long userId, Long cursor, int size);

    BoardPost createBoardPost(Long userId, String title, String content);

    BoardPost updateBoardPost(Long postId, String title, String content, Long userId);

    void deleteBoardPost(Long postId, Long userId);
}
