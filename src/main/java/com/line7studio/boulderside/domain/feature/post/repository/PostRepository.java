package com.line7studio.boulderside.domain.feature.post.repository;

import com.line7studio.boulderside.domain.feature.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}