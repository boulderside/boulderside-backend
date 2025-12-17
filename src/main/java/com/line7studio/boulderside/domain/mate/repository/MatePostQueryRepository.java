package com.line7studio.boulderside.domain.mate.repository;

import com.line7studio.boulderside.domain.mate.MatePost;
import com.line7studio.boulderside.domain.mate.enums.MatePostSortType;

import java.util.List;

public interface MatePostQueryRepository {
    List<MatePost> findMatePostsWithCursor(Long cursor, String subCursor, int size, MatePostSortType postSortType);
}
