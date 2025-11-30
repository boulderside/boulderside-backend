package com.line7studio.boulderside.domain.feature.post.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

	List<Post> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);
}
