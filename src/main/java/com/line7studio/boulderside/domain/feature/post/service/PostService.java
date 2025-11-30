package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.domain.feature.post.entity.Post;
import com.line7studio.boulderside.domain.feature.post.enums.PostSortType;
import com.line7studio.boulderside.domain.feature.post.enums.PostType;

import java.time.LocalDate;
import java.util.List;

public interface PostService {
	Post getPostById(Long postId);
	List<Post> getPostsWithCursor(Long cursor, String subCursor, int size, PostType postType, PostSortType sortType);
	List<Post> getPostsByUser(Long userId, Long cursor, int size);
    Post createPost(Long userId, String title, String content,PostType postType, LocalDate meetingDate);
    Post updatePost(Long postId, String title, String content, PostType postType, LocalDate meetingDate, Long userId);
    void deletePost(Long postId, Long userId);
}
