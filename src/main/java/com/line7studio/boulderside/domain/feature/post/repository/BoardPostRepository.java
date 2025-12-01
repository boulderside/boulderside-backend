package com.line7studio.boulderside.domain.feature.post.repository;

import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
    List<BoardPost> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<BoardPost> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);
}
