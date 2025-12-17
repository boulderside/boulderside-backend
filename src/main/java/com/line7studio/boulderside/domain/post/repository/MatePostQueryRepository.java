package com.line7studio.boulderside.domain.post.repository;

import com.line7studio.boulderside.domain.post.entity.MatePost;
import com.line7studio.boulderside.domain.post.enums.MatePostSortType;

import java.util.List;

public interface MatePostQueryRepository {
    List<MatePost> findMatePostsWithCursor(Long cursor, String subCursor, int size, MatePostSortType postSortType);
}
