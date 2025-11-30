package com.line7studio.boulderside.domain.feature.post.repository;

import com.line7studio.boulderside.domain.feature.post.entity.Post;
import com.line7studio.boulderside.domain.feature.post.enums.PostSortType;
import com.line7studio.boulderside.domain.feature.post.enums.PostType;

import java.util.List;

public interface PostQueryRepository {
    List<Post> findPostsWithCursor(Long cursor, String subCursor, int size, PostType postType, PostSortType postSortType);
}
