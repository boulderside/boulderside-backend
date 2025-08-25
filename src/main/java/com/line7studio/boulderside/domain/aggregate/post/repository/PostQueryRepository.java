package com.line7studio.boulderside.domain.aggregate.post.repository;

import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostSortType;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostType;

import java.util.List;

public interface PostQueryRepository {
    List<Post> findPostsWithCursor(Long cursor, int size, PostType postType, PostSortType postSortType);
}
