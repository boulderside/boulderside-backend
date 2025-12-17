package com.line7studio.boulderside.domain.post.repository;

import com.line7studio.boulderside.domain.post.entity.BoardPost;
import com.line7studio.boulderside.domain.post.enums.BoardPostSortType;

import java.util.List;

public interface BoardPostQueryRepository {
    List<BoardPost> findBoardPostsWithCursor(Long cursor, String subCursor, int size, BoardPostSortType postSortType);
}
