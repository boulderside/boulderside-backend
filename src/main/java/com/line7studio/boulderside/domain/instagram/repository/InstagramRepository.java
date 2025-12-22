package com.line7studio.boulderside.domain.instagram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.line7studio.boulderside.domain.instagram.Instagram;

public interface InstagramRepository extends JpaRepository<Instagram, Long> {
	/**
	 * 사용자 ID로 Instagram 게시물 목록 조회
	 */
	List<Instagram> findByUserId(Long userId);

	/**
	 * URL로 Instagram 게시물 조회 (중복 체크용)
	 */
	Optional<Instagram> findByUrl(String url);

	/**
	 * 전체 Instagram 페이징 조회
	 */
	@Query("SELECT i FROM Instagram i WHERE :cursor IS NULL OR i.id < :cursor ORDER BY i.id DESC")
	List<Instagram> findAllWithCursor(@Param("cursor") Long cursor, Pageable pageable);

	/**
	 * 사용자별 Instagram 페이징 조회
	 */
	@Query("SELECT i FROM Instagram i WHERE i.userId = :userId AND (:cursor IS NULL OR i.id < :cursor) ORDER BY i.id DESC")
	List<Instagram> findByUserIdWithCursor(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);
}