package com.line7studio.boulderside.domain.post.repository;

import com.line7studio.boulderside.domain.post.entity.MatePost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatePostRepository extends JpaRepository<MatePost, Long> {
    List<MatePost> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<MatePost> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);
}
