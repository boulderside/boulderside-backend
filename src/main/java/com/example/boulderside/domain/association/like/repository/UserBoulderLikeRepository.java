package com.example.boulderside.domain.association.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.domain.association.like.entity.UserBoulderLike;

public interface UserBoulderLikeRepository extends JpaRepository<UserBoulderLike, Long> {
	boolean existsByUserIdAndBoulderId(Long userId, Long boulderId);

	boolean deleteByUserIdAndBoulderId(Long userId, Long boulderId);
}
