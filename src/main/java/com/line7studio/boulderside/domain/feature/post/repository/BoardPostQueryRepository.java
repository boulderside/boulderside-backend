package com.line7studio.boulderside.domain.feature.post.repository;

import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import com.line7studio.boulderside.domain.feature.post.enums.BoardPostSortType;

import java.util.List;

public interface BoardPostQueryRepository {
    List<BoardPost> findBoardPostsWithCursor(Long cursor, String subCursor, int size, BoardPostSortType postSortType);
}
