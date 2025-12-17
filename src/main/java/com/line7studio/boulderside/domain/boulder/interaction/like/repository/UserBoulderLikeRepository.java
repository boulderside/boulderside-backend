package com.line7studio.boulderside.domain.boulder.interaction.like.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.boulder.interaction.like.UserBoulderLike;

public interface UserBoulderLikeRepository extends JpaRepository<UserBoulderLike, Long> {
	boolean existsByUserIdAndBoulderId(Long userId, Long boulderId);

	void deleteByUserIdAndBoulderId(Long userId, Long boulderId);

	long countByBoulderId(Long boulderId);

	void deleteAllByBoulderId(Long boulderId);

	List<UserBoulderLike> findByUserIdAndBoulderIdIn(Long userId, List<Long> boulderIds);

	List<UserBoulderLike> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

	List<UserBoulderLike> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);
}
