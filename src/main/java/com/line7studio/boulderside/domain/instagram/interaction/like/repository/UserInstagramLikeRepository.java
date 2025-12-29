package com.line7studio.boulderside.domain.instagram.interaction.like.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.instagram.interaction.like.UserInstagramLike;

public interface UserInstagramLikeRepository extends JpaRepository<UserInstagramLike, Long> {
	boolean existsByUserIdAndInstagramId(Long userId, Long instagramId);

	void deleteByUserIdAndInstagramId(Long userId, Long instagramId);

	long countByInstagramId(Long instagramId);

	void deleteAllByInstagramId(Long instagramId);

	List<UserInstagramLike> findByUserIdAndInstagramIdIn(Long userId, List<Long> instagramIds);

	List<UserInstagramLike> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

	List<UserInstagramLike> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);
}