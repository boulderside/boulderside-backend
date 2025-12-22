package com.line7studio.boulderside.domain.instagram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.line7studio.boulderside.domain.instagram.RouteInstagram;

public interface RouteInstagramRepository extends JpaRepository<RouteInstagram, Long> {
	/**
	 * 루트 ID로 연결된 Instagram 목록 조회
	 */
	List<RouteInstagram> findByRouteId(Long routeId);

	/**
	 * Instagram ID로 연결된 Route 목록 조회
	 */
	List<RouteInstagram> findByInstagramId(Long instagramId);

	/**
	 * 루트 ID와 Instagram ID로 연결 조회 (중복 체크용)
	 */
	Optional<RouteInstagram> findByRouteIdAndInstagramId(Long routeId, Long instagramId);

	/**
	 * 루트 ID와 Instagram ID로 연결 존재 여부 확인
	 */
	boolean existsByRouteIdAndInstagramId(Long routeId, Long instagramId);

	/**
	 * 루트 ID로 연결된 모든 RouteInstagram 삭제
	 */
	void deleteByRouteId(Long routeId);

	/**
	 * Instagram ID로 연결된 모든 RouteInstagram 삭제
	 */
	void deleteByInstagramId(Long instagramId);

	/**
	 * 특정 루트에 연결된 Instagram 개수 조회
	 */
	@Query("SELECT COUNT(ri) FROM RouteInstagram ri WHERE ri.routeId = :routeId")
	long countByRouteId(@Param("routeId") Long routeId);

	/**
	 * 루트별 Instagram 페이징 조회
	 */
	@Query("SELECT ri FROM RouteInstagram ri WHERE ri.routeId = :routeId AND (:cursor IS NULL OR ri.id < :cursor) ORDER BY ri.id DESC")
	List<RouteInstagram> findByRouteIdWithCursor(@Param("routeId") Long routeId, @Param("cursor") Long cursor, Pageable pageable);
}