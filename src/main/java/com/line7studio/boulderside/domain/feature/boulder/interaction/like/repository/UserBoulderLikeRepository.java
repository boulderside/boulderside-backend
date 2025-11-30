package com.line7studio.boulderside.domain.feature.boulder.interaction.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;

import java.util.List;

public interface UserBoulderLikeRepository extends JpaRepository<UserBoulderLike, Long> {
	boolean existsByUserIdAndBoulderId(Long userId, Long boulderId);

	void deleteByUserIdAndBoulderId(Long userId, Long boulderId);

	long countByBoulderId(Long boulderId);

	void deleteAllByBoulderId(Long boulderId);

    List<UserBoulderLike> findByUserIdAndBoulderIdIn(Long userId, List<Long> boulderIds);
}
