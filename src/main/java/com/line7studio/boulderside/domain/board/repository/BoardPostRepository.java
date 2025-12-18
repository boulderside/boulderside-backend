package com.line7studio.boulderside.domain.board.repository;

import com.line7studio.boulderside.domain.board.BoardPost;
import com.line7studio.boulderside.domain.enums.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
    List<BoardPost> findByUserIdAndStatusOrderByIdDesc(Long userId, PostStatus status, Pageable pageable);

    List<BoardPost> findByUserIdAndIdLessThanAndStatusOrderByIdDesc(Long userId, Long id, PostStatus status, Pageable pageable);

    Optional<BoardPost> findByIdAndStatus(Long postId, PostStatus status);
}
