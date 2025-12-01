package com.line7studio.boulderside.domain.feature.post.repository;

import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.enums.MatePostSortType;

import java.util.List;

public interface MatePostQueryRepository {
    List<MatePost> findMatePostsWithCursor(Long cursor, String subCursor, int size, MatePostSortType postSortType);
}
