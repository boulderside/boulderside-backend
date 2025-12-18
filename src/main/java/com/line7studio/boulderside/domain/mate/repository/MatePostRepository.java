package com.line7studio.boulderside.domain.mate.repository;

import com.line7studio.boulderside.domain.enums.PostStatus;
import com.line7studio.boulderside.domain.mate.MatePost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatePostRepository extends JpaRepository<MatePost, Long> {
    List<MatePost> findByUserIdAndStatusOrderByIdDesc(Long userId, PostStatus status, Pageable pageable);

    List<MatePost> findByUserIdAndIdLessThanAndStatusOrderByIdDesc(Long userId, Long id, PostStatus status, Pageable pageable);

    Optional<MatePost> findByIdAndStatus(Long postId, PostStatus status);

    List<MatePost> findAllByUserId(Long userId);
}
