package com.line7studio.boulderside.domain.board.repository;

import com.line7studio.boulderside.domain.board.BoardPost;
import com.line7studio.boulderside.domain.board.enums.BoardPostSortType;

import java.util.List;

public interface BoardPostQueryRepository {
    List<BoardPost> findBoardPostsWithCursor(Long cursor, String subCursor, int size, BoardPostSortType postSortType);
}
